package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Tick;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TickDao extends AbstractDao<Integer, Tick> {

    private static final String INSERT_EXTENDED = "INSERT IGNORE INTO Tick (tick, start, end) VALUES ";
    private static final String VALUES = "(?, ?, ?)";

    public TickDao() {
        super(Tick.class);
    }


    public void insert(List<Tick> ents) {
        if (ents.size() == 0) return;
        StringBuilder sql = new StringBuilder(INSERT_EXTENDED);

        Object[] args = new Object[ents.size() * 3];

        for (int i = 0; i < ents.size(); i++) {
            args[i * 3] = ents.get(i).getTick();
            args[i * 3 + 1] = ents.get(i).getStart();
            args[i * 3 + 2] = ents.get(i).getEnd();

            sql.append(VALUES);
            if (i < ents.size() - 1) {
                sql.append(", ");
            }
        }


        jdbcTemplate.update(sql.toString(), args);
    }


    public static final String SELECT_AVGS =
            "SELECT tick, start, end, trades, comms,  " +
                    "avg(commonPerPair) as commonPerPair, " +
                    "avg(commonBuysPerPair) as commonBuysPerPair, " +
                    "avg(commonSellsPerPair) as commonSellsPerPair, " +
                    "avg(commsPerTrader) as commsPerTrader, " +
                    "avg(commsPerPair) as commsPerPair " +
                    "FROM Tick WHERE ? < tick AND tick <= ?";

    public Tick selectAvgWhereId(long tick, int window) {
        return jdbcTemplate.queryForObject(
                SELECT_AVGS,
                new Object[]{tick-window, tick},
                new BeanPropertyRowMapper<>(Tick.class)
        );
    }

}
