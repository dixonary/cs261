package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TradeDao extends AbstractDao<Integer, Trade> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    //private static final String INSERT = "INSERT INTO Trade (time, buyer, seller, price, currency, size, symbol, sector, bid, ask) VALUES (?,?,?,?,?,?,?,?,?,?);";
    private static final String INSERT = "Call InsertTrade (?,?,?,?,?,?,?,?,?,?,?);";

    public TradeDao() {
        super(Trade.class);
    }

    //@Override
    public void insert(Trade ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getTime(),
                ent.getTick(),
                ent.getBuyer(),
                ent.getSeller(),
                ent.getPrice(),
                ent.getSize(),
                ent.getCurrency(),
                ent.getSymbol(),
                ent.getSector(),
                ent.getBid(),
                ent.getAsk()
        );
    }

    //@Override
    public void insert(final Iterable<Trade> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Trade ent : ents) {
            args.add(new Object[]{
                    ent.getTime(),
                    ent.getTick(),
                    ent.getBuyer(),
                    ent.getSeller(),
                    ent.getPrice(),
                    ent.getSize(),
                    ent.getCurrency(),
                    ent.getSymbol(),
                    ent.getSector(),
                    ent.getBid(),
                    ent.getAsk()
            });
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }




    /*@Override
    public List<Trade> selectAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Trade.class));
    }*/

    private static final String SELECT_BY_TRADER_ID = "SELECT * FROM Trade WHERE buyer = ? OR seller = ?";

    public List<Trade> findByTraderId(String id) {
        /*Map<String, Object> conds = new HashMap<>();

        conds.put("buyer", id);
        conds.put("seller", id);

        return selectWhere(conds);*/

        return jdbcTemplate.query(
                SELECT_BY_TRADER_ID,
                new Object[]{id, id},
                new BeanPropertyRowMapper<>(Trade.class)
        );
    }

    private static final String SELECT_BY_TRADER_ID_AND_LIMIT = "SELECT * FROM Trade WHERE buyer = ? OR seller = ? LIMIT ?, ?";

    public List<Trade> findByTraderIdAndLimit(String id, int start, int length) {
        return jdbcTemplate.query(
                SELECT_BY_TRADER_ID_AND_LIMIT,
                new Object[]{id, id, start, length},
                new BeanPropertyRowMapper<>(Trade.class)
        );
    }



    private static final String SELECT_COUNT_BY_TRADER_ID = "SELECT COUNT(*) FROM Trade WHERE buyer = ? OR seller = ?";

    public Integer countByTraderId(String id) {
        return jdbcTemplate.queryForObject(
                SELECT_COUNT_BY_TRADER_ID,
                new Object[]{id, id},
                Integer.class
        );
    }


}
