package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.RawEvent;
import team16.cs261.common.entity.Trade;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String INSERT = "CALL InsertTrade (?,?,?,?,?,?,?,?,?,?,?,?);";

    public TradeDao() {
        super(Trade.class);
    }

    //@Override
    public void insert(Trade ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getTime(),
                ent.getTick(),
                ent.getTimestamp(),
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

    private static Object[] getArgs(Trade ent) {
        return new Object[]{
                ent.getTime(),
                ent.getTimestamp(),
                ent.getTick(),
                ent.getBuyer(),
                ent.getSeller(),
                ent.getPrice(),
                ent.getSize(),
                ent.getCurrency(),
                ent.getSymbol(),
                ent.getSector(),
                ent.getBid(),
                ent.getAsk(),
                ent.getBuyerId(),
                ent.getSellerId(),
                ent.getSymbolId(),
                ent.getSectorId()
        };
    }



    int columns = 16;
    String columnsStmt = "(time, timestamp, tick, buyer, seller, price, size, currency, symbol, sector, bid, ask, buyerId, sellerId, symbolId, sectorId)";
    public void insert(List<Trade> ents) {
        if (ents.size() == 0) return;
        StringBuilder sql = new StringBuilder("INSERT INTO Trade "+columnsStmt+" VALUES ");



        String values = genValuesStmt(columns);

        Object[] args = new Object[ents.size() * columns];

        for (int i = 0; i < ents.size(); i++) {
            sql.append(values);
            if (i < ents.size() - 1) {
                sql.append(", ");
            }

            Trade ent = ents.get(i);

            Object[] entArgs = getArgs(ent);

            for (int c = 0; c < columns; c++) {
                args[i * columns + c] = entArgs[c];
            }

            //args[i * 3] = ents.get(i).getType().name();
            //args[i * 3 + 1] = ents.get(i).getTime();
            //args[i * 3 + 2] = ents.get(i).getRaw();


        }

        jdbcTemplate.update(sql.toString(), args);
    }


    //@Override
/*    public void insert(final Iterable<Trade> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Trade ent : ents) {
            args.add(getArgs(ent));
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }*/




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
