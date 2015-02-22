package team16.cs261.common.dao;

import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.TraderPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TraderPairDao extends AbstractDao<Integer, TraderPair> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    private static final String INSERT = "INSERT INTO TraderPair (email1, email2) VALUES (?,?);";


    public TraderPairDao() {
        super(TraderPair.class);
    }

    public void insert(TraderPair ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getEmail1(),
                ent.getEmail2()
        );
    }

    public void insert(final Iterable<TraderPair> ents) {
        List<Object[]> args = new ArrayList<>();
        for (TraderPair ent : ents) {
            args.add(new Object[]{
            });
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }

    public static final String INSERT_OR_UPDATE =
            "INSERT INTO TraderStock (email, symbol, volume, lastUpdate) VALUES(?, ?, ?, ?) " +
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
