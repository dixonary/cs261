package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Cluster;
import team16.cs261.dal.entity.factor.Factor;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class FactorDao extends AbstractDao {

    private static final String SELECT = "SELECT * FROM Factor";

    @Override
    public List<Factor> findAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Factor.class));
    }

    private static final String SELECT_BY_ID = "SELECT * FROM Factor WHERE factorId = ?";

    @Override
    public Factor findById(int id) {
        return jdbcTemplate.queryForObject(
                SELECT_BY_ID, new Object[]{id},
                new BeanPropertyRowMapper<>(Factor.class));
    }

    private static final String SELECT_BY_CLUSTER_ID = "SELECT * FROM Factor WHERE clusterId = ?";

    public List<Factor> findByClusterId(int clusterId) {
        return jdbcTemplate.query(
                SELECT_BY_CLUSTER_ID, new Object[]{clusterId},
                new BeanPropertyRowMapper<>(Factor.class));
    }
}
