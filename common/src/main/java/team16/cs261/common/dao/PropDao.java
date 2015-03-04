package team16.cs261.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

    public <E> E getProperty(String key, Class<E> type, E def) {
        try {
            return jdbcTemplate.queryForObject(GET_PROPERTY, new Object[]{key}, type);
        } catch (EmptyResultDataAccessException e) {
            return def;
        }
    }

    public static final String GET_PROPERTIES = "SELECT `key`, `value` FROM Prop";

    public Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();

        SqlRowSet rows = jdbcTemplate.queryForRowSet(GET_PROPERTIES);
        while (rows.next()) {
            props.put(rows.getString(1), rows.getString(2));
        }

        return props;
    }
}
