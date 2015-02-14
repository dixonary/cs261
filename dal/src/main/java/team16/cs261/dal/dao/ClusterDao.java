package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Cluster;
import team16.cs261.dal.entity.Trade;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class ClusterDao extends AbstractDao<Cluster> {





    private static final String SELECT = "SELECT * FROM Cluster";

    @Override
    public List<Cluster> findAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Cluster.class));
    }


    private static final String SELECT_BY_ID = "SELECT * FROM Cluster WHERE clusterId = ?";

    @Override
    public Cluster findById(int id) {
        return jdbcTemplate.queryForObject(
                SELECT_BY_ID, new Object[]{id},
                new BeanPropertyRowMapper<>(Cluster.class));
    }
}
