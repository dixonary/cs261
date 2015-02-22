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
public class CounterDao extends AbstractDao<Integer, Counter> {

    private static final String INSERT = "INSERT INTO Counter (count) VALUES (0);";

    public CounterDao() {
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

    public static final String INSERT_OR_UPDATE =
            "INSERT INTO TraderStock (email, symbol, volume, lastUpdate) " +
                    "VALUES(?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "volume=volume+VALUES(volume)";

    public void updateTraderStock(List<Trade> tradeEnts) {
        long time = System.currentTimeMillis();

        List<Object[]> args = new ArrayList<>();
        for (Trade tradeEnt : tradeEnts) {
            args.add(new Object[]{tradeEnt.getBuyer(), tradeEnt.getSymbol(), tradeEnt.getSize(), time});
            args.add(new Object[]{tradeEnt.getSeller(), tradeEnt.getSymbol(), -tradeEnt.getSize(), time});
        }

        jdbcTemplate.batchUpdate(INSERT_OR_UPDATE, args);
    }
}
