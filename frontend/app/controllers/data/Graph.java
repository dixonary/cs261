package controllers.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.graph.EdgeDto;
import models.graph.NodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.querydsl.entity.QCluster;
import util.JsonNodeRowMapper;
import views.html.helper.requireJs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.mvc.Http.Context.Implicit.request;
import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Graph {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QueryDslJdbcTemplate template;

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TradeDao tradeDao;



/*    public static final String SELECT_NODES = "SELECT CAST(N.id AS CHAR(50)) AS id FROM Node N JOIN Trader T ON N.id = T.id";
    public static final String SELECT_EDGES =
            "SELECT CAST(E.id AS CHAR(50)) AS id, " +
                    "CAST(source AS CHAR(50)) AS source, " +
                    "CAST(target AS CHAR (50)) AS target, " +
                    "5 AS size " +
                    "FROM Edge E JOIN TraderPair TTE ON E.id = TTE.id AND TTE.commWgt > 0";*/

/*    public static final String SELECT_NODES =
            "SELECT T.id, T.email AS label FROM Node N JOIN Trader T ON N.id = T.id";
    public static final String SELECT_EDGES =
            "SELECT `source` AS `from`, target AS `to`, weight AS value " +
                    "FROM Edge E NATURAL JOIN TraderPair TTE ORDER BY weight DESC LIMIT 12";

    public Result graph() {
        List<JsonNode> nodes = jdbcTemplate.query(SELECT_NODES, new Object[0], new JsonNodeRowMapper());
        List<JsonNode> edges = jdbcTemplate.query(SELECT_EDGES, new Object[0], new JsonNodeRowMapper());

        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));
        return ok(data);
    }*/


    public static final String SELECT_TRADERS =
            "SELECT " +
                    "T.id, " +
                    "T.email AS label, " +
                    //"T.id AS label, " +
                    "'trader' AS `group` " +
                    "FROM Node N JOIN Trader T ON N.id = T.id";

    public static final String SELECT_SYMBOLS =
            "SELECT S.id, S.symbol AS label, 'symbol' AS `group` FROM Node N JOIN Symbol S ON N.id = S.id";

    public static final String SELECT_SECTORS =
            "SELECT S.id, S.sector AS label, 'sector' AS `group` FROM Node N JOIN Sector S ON N.id = S.id";

    public static final String SELECT_EDGES =
            "SELECT F.id, TP.id as edge, `trader1Id` AS `from`, trader2Id AS `to`, " +
                    //"score * 100 as label, "+
                    //"centile AS value, " +
                    "CONCAT(factor, ': ',value) AS label, " +
                    "CONCAT(score * 100, ' (', sig, '%)') as title, "+
                    //"score  as value, "+
                    "240 as length "+
                    //"centile AS value, CONCAT(factor, ': ',value) AS label, " +
                    //"sig AS title " +
                    "FROM TraderPair TP JOIN Factor F ON TP.id = F.edge WHERE tick = ? " +
                    "/*GROUP BY TTE.id HAVING count(*) >= 2*/";

    public static final String SELECT_TS_EDGES =
            "SELECT `traderId` AS `from`, symbolId AS `to` " +
                    "FROM TraderSymbol LIMIT 10";

    public static final String SELECT_CLUSTER_EDGES =
            "SELECT `source` AS `from`, target AS `to`, CE.weight AS value, CONCAT('Cls: ', CE.weight) AS label " +
                    "FROM Edge E JOIN TickClusterEdge CE ON E.id = CE.edge WHERE tick = ?";


    public Result graph(int tick, String clusters, String factors) {


        return ok("null");
    }


    public Result factors(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        List<EdgeDto> traderPairs = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

        nodes.addAll(traders);
        edges.addAll(traderPairs);


        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));
        return ok(data);
    }

    public Result factorsMcl(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        List<EdgeDto> traderPairs = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

        StringBuilder sb = new StringBuilder();
        for(EdgeDto tp : traderPairs) {
            sb.append(tp.from).append("\t").append(tp.to).append("\t").append(tp.value).append("\n");
        }

        nodes.addAll(traders);
        edges.addAll(traderPairs);


        return ok(sb.toString());
    }

    public Result clusters(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        List<Integer> clusterNodes;

        try {
            int clusterId = Integer.parseInt(request().getQueryString("cluster"));

            clusterNodes = jdbcTemplate.queryForList(
                    "SELECT node FROM Cluster C JOIN ClusterNode CN ON C.id = CN.cluster WHERE tick = ? AND C.id = ?",
                    new Object[]{tick, clusterId} , Integer.class);
        }catch(Exception ignored) {

            clusterNodes = jdbcTemplate.queryForList(
                    "SELECT node FROM Cluster C JOIN ClusterNode CN ON C.id = CN.cluster WHERE tick = ?",
                    new Object[]{tick} , Integer.class);
        }

        //System.out.println("cls: "+ clusterId);


        //List<Integer> clusterNodeIds =template.newSqlQuery().from(QCluster.clus)







        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        List<EdgeDto> traderPairs = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

        //nodes.addAll(traders);
        //edges.addAll(traderPairs);
        for(NodeDto t : traders) {
            if(clusterNodes.contains(t.id)) nodes.add(t);
        }

        for(EdgeDto tp : traderPairs) {
            if(clusterNodes.contains(tp.from) && clusterNodes.contains((tp.to)))
                edges.add(tp);
        }


        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));
        return ok(data);
    }

    public Result graph(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        String clusters = request().getQueryString("clusters");
        String factors = request().getQueryString("factors");

        //List<Integer> clusterNodeIds =template.newSqlQuery().from(QCluster.clus)


        List<Integer> clusterNodes = jdbcTemplate.queryForList(
                "SELECT node FROM Cluster C JOIN ClusterNode CN ON C.id = CN.cluster WHERE tick = ?",
                new Object[]{tick}, Integer.class);

        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        List<EdgeDto> traderPairs = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

        //nodes.addAll(traders);
        //edges.addAll(traderPairs);
        for(NodeDto t : traders) {
            if(clusterNodes.contains(t.id)) nodes.add(t);
        }

        for(EdgeDto tp : traderPairs) {
            if(clusterNodes.contains(tp.from) && clusterNodes.contains((tp.to)))
                edges.add(tp);
        }


        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));
        return ok(data);
    }


    public Result graph2(int tick) {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();


        List<NodeDto> traders = jdbcTemplate.query(SELECT_TRADERS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        //List<NodeDto> symbols = jdbcTemplate.query(SELECT_SYMBOLS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));
        //List<NodeDto> sectors = jdbcTemplate.query(SELECT_SECTORS, new Object[]{}, new BeanPropertyRowMapper<>(NodeDto.class));

        nodes.addAll(traders);
        //nodes.addAll(symbols);
        //nodes.addAll(sectors);

        Set<Integer> clusterNodes = new HashSet<>();

        List<EdgeDto> ttEdges = jdbcTemplate.query(SELECT_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));
        List<EdgeDto> tsEdges = jdbcTemplate.query(SELECT_TS_EDGES, new Object[]{}, new BeanPropertyRowMapper<>(EdgeDto.class));

        //List<EdgeDto> clusterEdges = jdbcTemplate.query(SELECT_CLUSTER_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(EdgeDto.class));

/*        for (EdgeDto e : clusterEdges) {
            clusterNodes.add(e.from);
            clusterNodes.add(e.to);
        }*/


        for (NodeDto n : traders) {
            if (!clusterNodes.contains(n.id)) continue;

            nodes.add(n);
        }

        for (EdgeDto e : ttEdges) {
            if (!clusterNodes.contains(e.from)) continue;
            if (!clusterNodes.contains(e.to)) continue;

            //edges.add(e);
        }

        //edges.addAll(clusterEdges);

        edges.addAll(ttEdges);
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

        return ok(data);
    }





}