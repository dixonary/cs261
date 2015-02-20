package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Trade;
import team16.cs261.dal.entity.TraderStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TraderStockDao extends AbstractDao<Integer, TraderStock> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    private static final String INSERT = "INSERT INTO TraderStock (symbol, email, volume) VALUES (?,?,?);";


    public TraderStockDao() {
        super(TraderStock.class);
    }

    public void insert(TraderStock ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getSymbol(),
                ent.getEmail(),
                ent.getVolume(),
                System.currentTimeMillis()
        );
    }

    public void insert(final Iterable<TraderStock> ents) {
        List<Object[]> args = new ArrayList<>();
        for (TraderStock ent : ents) {
            args.add(new Object[]{
            });
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }



    private static final String SELECT_BY_TRADER_ID = "SELECT * FROM Trade WHERE buyer = ? OR seller = ?";
    private static final String SELECT_BY_TRADER_ID_AND_LIMIT = "SELECT * FROM Trade WHERE buyer = ? OR seller = ? LIMIT ?, ?";


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
