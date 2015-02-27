package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.RawEvent;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class RawEventDao extends AbstractDao<Integer, RawEvent> {

    private static final String INSERT = "INSERT INTO RawTrade (raw) VALUES (?);";

    public RawEventDao() {
        super(RawEvent.class);
    }

    public List<RawEvent> selectAllLimit(int length) {
        return jdbcTemplate.query(
                "SELECT * FROM RawEvent ORDER BY time ASC LIMIT ? ",
                new Object[]{length},
                new BeanPropertyRowMapper<>(RawEvent.class)
        );
    }

    public static final String VALUES = "(?, ?, ?)";

    public void insert(List<RawEvent> ents) {
        if(ents.size()==0)return;
        StringBuilder sql = new StringBuilder("INSERT INTO RawEvent (type, time, raw) VALUES ");

        Object[] args = new Object[ents.size() * 3];

        for (int i = 0; i < ents.size(); i++) {
            args[i * 3] = ents.get(i).getType().name();
            args[i * 3 + 1] = ents.get(i).getTime();
            args[i * 3 + 2] = ents.get(i).getRaw();

            sql.append(VALUES);
            if (i < ents.size() - 1) {
                sql.append(", ");
            }
        }

        jdbcTemplate.update(sql.toString(), args);

    }

}
