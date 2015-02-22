package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Cluster;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class ClusterDao extends AbstractDao<Integer, Cluster> {


    public ClusterDao() {
        super(Cluster.class);
    }


    private static final String SELECT_COUNT_WHERE_DATERANGE = "SELECT COUNT(*) FROM Cluster WHERE time BETWEEN ? AND ?";

    public Integer selectCountWhereDaterange(long dateFrom, long dateTo) {
        return jdbcTemplate.queryForObject(
                SELECT_COUNT_WHERE_DATERANGE,
                new Object[]{dateFrom, dateTo},
                Integer.class
        );
    }

    private static final String SELECT_WHERE_DATERANGE = "SELECT * FROM Cluster WHERE time BETWEEN ? AND ? LIMIT ?, ?";

    public List<Cluster> selectWhereDaterange(long dateFrom, long dateTo, int offset, int limit) {
        return jdbcTemplate.query(
                SELECT_WHERE_DATERANGE,
                new Object[]{dateFrom, dateTo, offset, limit},
                new BeanPropertyRowMapper<>(Cluster.class)
        );
    }

}
