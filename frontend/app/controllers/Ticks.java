package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.graph.EdgeDto;
import models.graph.NodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.TickDao;
import team16.cs261.common.entity.Tick;

import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

@org.springframework.stereotype.Controller
public class Ticks {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TickDao ticks;

    public Result collection() {
        return ok(views.html.trades.collection.render());
    }

    public Result element(int id) {
        Tick tick = ticks.selectWhereId(id);

        if (tick == null) {
            return redirect(controllers.routes.Application.index());
        }

        return ok(views.html.ticks.element.render(tick));
    }


    public static final String SELECT_TRADERS =
            "SELECT T.id, T.email AS label, 'trader' AS `group` FROM Node N JOIN Trader T ON N.id = T.id";

    public static final String SELECT_SYMBOLS =
            "SELECT S.id, S.symbol AS label, 'symbol' AS `group` FROM Node N JOIN Symbol S ON N.id = S.id";

    public static final String SELECT_SECTORS =
            "SELECT S.id, S.sector AS label, 'sector' AS `group` FROM Node N JOIN Sector S ON N.id = S.id";

    public static final String SELECT_EDGES =
            "SELECT `trader1Id` AS `from`, trader2Id AS `to`, 0.001 AS value, CONCAT(factor, ': ',value) AS label, sig AS title " +
                    "FROM TraderPair TTE JOIN Factor F ON TTE.id = F.edge WHERE tick = ? /*GROUP BY TTE.id HAVING count(*) >= 2*/ ORDER BY comms DESC LIMIT 50";

    public static final String SELECT_TS_EDGES =
            "SELECT `traderId` AS `from`, symbolId AS `to` " +
                    "FROM TraderSymbol LIMIT 10";

    public static final String SELECT_CLUSTER_EDGES =
            "SELECT `source` AS `from`, target AS `to`, CE.weight AS value, CONCAT('Cls: ', CE.weight) AS label " +
                    "FROM Edge E JOIN ClusterEdge CE ON E.id = CE.edge WHERE tick = ?";

    public Result graph(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();


        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        //List<NodeDto> symbols = jdbcTemplate.query(SELECT_SYMBOLS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        //List<NodeDto> sectors = jdbcTemplate.query(SELECT_SECTORS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));

        //nodes.addAll(traders);
        //nodes.addAll(symbols);
        //nodes.addAll(sectors);

        Set<Integer> clusterNodes = new HashSet<>();

        List<EdgeDto> ttEdges = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));
        List<EdgeDto> tsEdges = jdbcTemplate.query(SELECT_TS_EDGES, new Object[]{}, new BeanPropertyRowMapper<>(EdgeDto.class));
        List<EdgeDto> clusterEdges = jdbcTemplate.query(SELECT_CLUSTER_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

        for (EdgeDto e : clusterEdges) {
            clusterNodes.add(e.from);
            clusterNodes.add(e.to);
        }


        for (NodeDto n : traders) {
            if (!clusterNodes.contains(n.id)) continue;

            nodes.add(n);
        }

        for (EdgeDto e : ttEdges) {
            if (!clusterNodes.contains(e.from)) continue;
            if (!clusterNodes.contains(e.to)) continue;

            edges.add(e);
        }

        edges.addAll(clusterEdges);

        //edges.addAll(ttEdges);
        //edges.addAll(tsEdges);
        //edges.addAll(clusterEdges);


        EdgeDto selfloop = new EdgeDto();
        selfloop.from = 27;
        selfloop.to = 27;
        selfloop.label = "self-loop";
        edges.add(selfloop);

        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));


        for (EdgeDto edge : edges) {

        }


        //SparseMatrix sm = new SparseMatrix();

        SqlRowSet trEdges = jdbcTemplate.queryForRowSet(
                //"SELECT source, target, commWgt FROM TraderPair TTE JOIN Edge E WHERE TTE.id = E.id AND commWgt > 0"
                /*"SELECT source, target, weight FROM TraderPair TTE NATURAL JOIN Edge E WHERE weight > 0"*/
                "SELECT source, target, weight FROM TraderPair TTE NATURAL JOIN Edge E ORDER BY weight DESC LIMIT 12"
        );
        //Dataset ds = new DefaultDataset();
        //Set<Integer> nodeSet = new TreeSet<>();
/*
        while (trEdges.next()) {
            int n1 = trEdges.getInt(1);
            int n2 = trEdges.getInt(2);
            float v = trEdges.getFloat(3);

//            nodeSet.add(n1);
//            nodeSet.add(n2);

            //ds.add(n1, new SparseInstance(n1));
            //ds.add(n2, new SparseInstance(n2));


            sm.set(n1, n2, v);
            sm.set(n2, n1, v);
        }*/


        return ok(data);

        //return play.mvc.Controller.ok(views.html.ticks.element.render());
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Tick";
    private static final String SELECT_AND_LIMIT_COUNT = "SELECT COUNT(*) FROM Tick LIMIT ?, ?";

    public Result query() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        int recordsTotal = ticks.selectCountAll();
        List<Tick> data = ticks.selectAllLimit(start, length);


        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return ok(response);
    }


}