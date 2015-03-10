package team16.cs261.common.dao;

import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Counter;
import team16.cs261.common.entity.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class CountsDao extends AbstractDao<Integer, Counter> {

    private static final String INSERT = "INSERT INTO Counts (intvl, count) VALUES (0);";

    public CountsDao() {
        super(Counter.class);
    }

    public void insert(Counter ent) {
        jdbcTemplate.update(
                INSERT, 0
        );
    }

    public void insert(final Iterable<Counter> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Counter ent : ents) {
            args.add(new Object[]{});
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }

    private static final String SELECT_UNSEALED = "SELECT max(intvl) FROM Counts WHERE sealed = true";

    public Integer lastSealed() {
        //return  jdbcTemplate.queryForList(SELECT_UNSEALED, Integer.class);
        return  jdbcTemplate.queryForObject(SELECT_UNSEALED, Integer.class);
    }

    public static final String INSERT_OR_UPDATE =
            "INSERT INTO TraderStock (email, symbol, volume, lastUpdate) " +
                    "VALUES(?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "volume=volume+VALUES(volume)";


}
