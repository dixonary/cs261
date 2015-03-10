package controllers.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.types.Projections;
import models.Point;
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
import team16.cs261.common.meta.FactorClass;
import team16.cs261.common.querydsl.entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Dashboard {

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

    QCluster cl = QCluster.Cluster;

    public Result clusters(int since, int count) {
        ObjectNode res = Json.newObject();

        SQLQuery latest = template.newSqlQuery()
                .from(cl)
                        //.where(cl.status.eq("UNSEEN"))
                .where(cl.id.gt(since))
                .orderBy(cl.id.desc())
                .limit(count);

        List<Cluster> data = template.query(latest,
                Projections.bean(Cluster.class, cl.id, cl.tick, cl.time, cl.status)
        );

        long newSince = since;
        for (Cluster cl : data) {
            newSince = Math.max(newSince, cl.getId());
            System.out.println("new: " + newSince);
        }

        res.put("since", since);
        res.put("latest", newSince);
        res.put("clusters", Json.toJson(data));


        return ok(Json.toJson(res));
    }

    public Result activity() {
        int ivalRate = 10;
        int ivals = 3 * 60;

        QCounts qC = QCounts.Counts;

        SQLQuery query = template.newSqlQuery()
                .from(qC).orderBy(qC.intvl.desc())
                .limit(ivals);


        List<Point> data = template.query(query,
                Projections.constructor(
                        Point.class,
                        qC.intvl.longValue().multiply(10000),
                        qC.tradesRead.doubleValue().divide(ivalRate),
                        qC.commsRead.doubleValue().divide(ivalRate))
        );

        double[][] tradesRead = new double[data.size()][2];
        double[][] commsRead = new double[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            Point p = data.get(i);

            tradesRead[i][0] = p.getX();
            tradesRead[i][1] = p.getY1();

            commsRead[i][0] = p.getX();
            commsRead[i][1] = p.getY2();

            //points[p.getX()][i] = p.getY1();
            //points[p.getX()][i] = p.getY1();
        }

        double[][][] points = {tradesRead, commsRead};

        return ok(Json.toJson(points));
    }

    public Result events() {

        QTick t = QTick.Tick;

        SQLQuery query = template.newSqlQuery()
                .from(t).orderBy(t.tick.desc()).limit(60);


        List<Point> data = template.query(query,
                Projections.constructor(Point.class, t.tick, t.trades, t.comms)
        );

        double[][] trades = new double[data.size()][2];
        double[][] comms = new double[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            Point p = data.get(i);

            trades[i][0] = p.getX();
            trades[i][1] = p.getY1();

            comms[i][0] = p.getX();
            comms[i][1] = p.getY2();

            //points[p.getX()][i] = p.getY1();
            //points[p.getX()][i] = p.getY1();
        }

        double[][][] points = {trades, comms};

        return ok(Json.toJson(points));
    }

    public Result rates() {


        for (FactorClass fc : FactorClass.getImplemented()) {
            ObjectNode series = Json.newObject();

            series.put("label", fc.getLabel());





        }


        return ok("nada");
    }


    public Result freqs(final String factor) {
        //final String factor = "COMMON_BUYS";

/*        if(factor==null){
            for(FactorClass fc : FactorClass.getImplemented()) {

            }
        }*/

        QFactorFreq ff = QFactorFreq.FactorFreq;
        FactorClass fc = FactorClass.valueOf(factor);

        ObjectNode series = Json.newObject();
        series.put("label", fc.getLabel());


        SQLQuery query = template.newSqlQuery()
                .from(ff).where(ff.factor.eq(factor));
        //.groupBy(ff.x);//.list(ff.x, ff.x.sum());

        Double fxSum = template.queryForObject(query, ff.fx.sum().doubleValue());

        List<Point> data = template.query(query.groupBy(ff.x),
                Projections.constructor(Point.class,
                        ff.x.longValue(),
                        ff.fx.sum().castToNum(Double.class).divide(fxSum),
                        ff.fx.sum().doubleValue())
        );

/*        SQLQuery query = template.newSqlQuery()
                .from(ff).where(ff.factor.eq(factor))
                .groupBy(ff.tick, ff.x);//.list(ff.x, ff.x.sum());

        List<Point> data = template.query(query,
                Projections.constructor(Point.class,
                        ff.x,
                        ff.fx.sum().castToNum(Double.class),
                        ff.fx.sum())
        );*/

        double sum = 0;

        double[][] points = new double[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            Point p = data.get(i);

            points[i][0] = p.getX();
            points[i][1] = p.getY1();

            sum += p.getY1();
        }

        series.put("data", Json.toJson(points));

        return ok(series);
    }


    public Result cluster(int id) {
        return ok("null");
    }

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
                    "FROM Edge E JOIN TickClusterEdge CE ON E.id = CE.edge WHERE tick = ?";


    public Result graph(int tick, String clusters, String factors) {


        return ok("null");
    }


    public Result graph2(int tick, String clusters, String factors) {
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

            //edges.add(e);
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

        return ok(data);
    }


    public static final String SELECT_TTE_COUNT = "SELECT count(*) FROM TraderPair";
    public static final String SELECT_TTE_LIMIT =
            "SELECT id, source, target, trader1, trader2, comms, commWgt " +
                    "FROM TraderPair TTE " +
                    "NATURAL JOIN Edge E " +
                    "ORDER BY commWgt DESC LIMIT ?";

    public double[][] getCommsGraph() {
        final double[][] matrix;

        Integer maxNode = jdbcTemplate.queryForObject("SELECT max(id) FROM Node", Integer.class);
        matrix = new double[maxNode][maxNode];

        Integer edges = jdbcTemplate.queryForObject(SELECT_TTE_COUNT, Integer.class);
        int limit = (int) (0.05 * edges);

        jdbcTemplate.query(SELECT_TTE_LIMIT, new Object[]{limit}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

                int n1 = rs.getInt(2);
                int n2 = rs.getInt(3);
                float commWgt = rs.getFloat(7);

                System.out.println(n1 + "\t" + n2 + "\t" + commWgt);

                if (n1 < n2) {
                    matrix[n1][n2] = commWgt;
                } else {
                    matrix[n2][n1] = commWgt;
                }


            }
        });

        return matrix;
    }

    public Result comms() {

        double[][] matrix = getCommsGraph();

        return ok(Json.toJson(matrix));
    }
}