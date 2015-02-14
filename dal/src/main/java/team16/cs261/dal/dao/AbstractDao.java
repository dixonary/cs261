package team16.cs261.dal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by martin on 14/02/15.
 */
public class AbstractDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

}
