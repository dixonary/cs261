package team16.cs261.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Tick;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class LogDao {

    @Autowired
    JdbcTemplate jdbc;

    private static final String INSERT_LOG = "INSERT INTO Log (time, type, message) VALUES (?,?,?) ";
    private static final String VALUES = "(?, ?, ?)";

    public LogDao() {

    }


    public void log(String type, String message) {
        jdbc.update(INSERT_LOG, System.currentTimeMillis(), type, message);
    }


}
