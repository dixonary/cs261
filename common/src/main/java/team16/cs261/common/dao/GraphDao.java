package team16.cs261.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import team16.cs261.common.dto.AdjacencyList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class GraphDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public GraphDao() {
    }




    public static final String SELECT_TTE_COUNT = "SELECT count(*) FROM TraderPair";
    public static final String SELECT_TTE_LIMIT =
            "SELECT id, source, target, trader1, trader2, comms, commWgt " +
                    "FROM TraderPair TTE " +
                    "NATURAL JOIN Edge E " +
                    "ORDER BY commWgt DESC LIMIT ?";

    public AdjacencyList getComms() {


        int maxNode = jdbcTemplate.queryForObject("SELECT max(id) FROM Node", Integer.class);

        final AdjacencyList adjList = new AdjacencyList();

        System.out.println("max node: "+ maxNode);
        //System.out.println("Size: " + adjList.list.size());
        //System.out.println("set : " + adjList.list.get(140));

        Integer edges = jdbcTemplate.queryForObject(SELECT_TTE_COUNT, Integer.class);
        int limit = (int) (0.05 * edges);

        jdbcTemplate.query(SELECT_TTE_LIMIT, new Object[]{limit}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {

                int n1 = rs.getInt(2);
                int n2 = rs.getInt(3);
                float commWgt = rs.getFloat(7);

                //System.out.println(n1 + "\t" + n2 + "\t" + commWgt);

                adjList.addEdge(n1, n2);
            }
        });

        return adjList;
    }

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

}
