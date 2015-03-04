package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ClusterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.GraphDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.entity.Cluster;
import team16.cs261.common.entity.factor.Factor;
import util.JsonNodeRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Graph {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TradeDao tradeDao;

    @Autowired
        GraphDao graphDao;

    public Result index() {
        return ok(views.html.graph.render());
    }

/*    public static final String SELECT_NODES = "SELECT CAST(N.id AS CHAR(50)) AS id FROM Node N JOIN Trader T ON N.id = T.id";
    public static final String SELECT_EDGES =
            "SELECT CAST(E.id AS CHAR(50)) AS id, " +
                    "CAST(source AS CHAR(50)) AS source, " +
                    "CAST(target AS CHAR (50)) AS target, " +
                    "5 AS size " +
                    "FROM Edge E JOIN TraderPair TTE ON E.id = TTE.id AND TTE.commWgt > 0";*/

    public static final String SELECT_NODES =
            "SELECT T.id, T.email AS label FROM Node N JOIN Trader T ON N.id = T.id";
    public static final String SELECT_EDGES =
            "SELECT `source` AS `from`, target AS `to`, weight AS value " +
                    "FROM Edge E NATURAL JOIN TraderPair TTE ORDER BY weight desc LIMIT 12";

    public Result graph() {
        List<JsonNode> nodes = jdbcTemplate.query(SELECT_NODES, new Object[0], new JsonNodeRowMapper());
        List<JsonNode> edges = jdbcTemplate.query(SELECT_EDGES, new Object[0], new JsonNodeRowMapper());

        ObjectNode data = Json.newObject();
        data.put("nodes", Json.toJson(nodes));
        data.put("edges", Json.toJson(edges));
        return ok(data);
    }

    public static final String SELECT_TT_EDGES = "SELECT source, target, (commWgt) AS wgt " +
            "FROM TraderPair TTE JOIN Edge E ON TTE.id = E.id WHERE commWgt > 0";

    public Result mcl() {

        StringBuilder sb = new StringBuilder();

        SqlRowSet trEdges = jdbcTemplate.queryForRowSet(SELECT_TT_EDGES);
        while (trEdges.next()) {
            int n1 = trEdges.getInt(1);
            int n2 = trEdges.getInt(2);
            float v = trEdges.getFloat(3);

            sb.append(n1).append("\t").append(n2).append("\t").append(v).append("\n");
            //sb.append(n2).append("\t").append(n1).append("\t").append(v).append("\n");
        }

        return ok(sb.toString());
    }


    public Result matlab() {

        int size = 151;

        int[][] weights = new int[size][size];

        SqlRowSet trEdges = jdbcTemplate.queryForRowSet(SELECT_TT_EDGES);
        while (trEdges.next()) {
            int n1 = trEdges.getInt(1);
            int n2 = trEdges.getInt(2);
            int v = trEdges.getInt(3);

            weights[n1][n2] = v;
            weights[n2][n1] = v;
        }

        StringBuilder data = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data.append(weights[i][j]).append(" ");
            }
            data.append("\n");
        }

        return ok(data.toString());
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

                if(n1 < n2) {
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