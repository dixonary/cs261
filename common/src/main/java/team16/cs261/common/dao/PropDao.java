package team16.cs261.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sun.org.mozilla.javascript.internal.json.JsonParser;
import team16.cs261.common.entity.RawTrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class PropDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PropDao() {
    }

    public static final String SET_PROPERTY = "INSERT INTO Prop VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE `value` = VALUES(`value`);";

    public void setProperty(String key, Object value) {
        jdbcTemplate.update(SET_PROPERTY, key, value);
    }


    public static final String GET_PROPERTY = "SELECT `value` FROM Prop WHERE `key` = ?";

    public <E> E getProperty(String key, Class<E> type) {
        return jdbcTemplate.queryForObject(GET_PROPERTY, new Object[]{key}, type);
    }
}
