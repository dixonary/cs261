package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Trade;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TradeDao extends AbstractDao<Trade> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    private static final String INSERT = "INSERT INTO Trade (time, buyer, seller, price, currency, size, symbol, sector, bid, ask) VALUES (?,?,?,?,?,?,?,?,?,?);";


    public TradeDao() {


    }

    @Override
    public void insert(Trade ent) {
        jdbcTemplate.update(INSERT, ent.getTime(),
                ent.getBuyer(),
                ent.getSeller(),
                ent.getPrice(),
                ent.getCurrency(),
                ent.getSize(),
                ent.getSymbol(),
                ent.getSector(),
                ent.getBid(),
                ent.getAsk());
    }

    @Override
    public List<Trade> findAll() {
        List<Trade> results = jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Trade.class));

        return results;
    }


    public List<Trade> selectAndLimit(int offset, int length) {
        List<Trade> results = jdbcTemplate.query(
                SELECT_AND_LIMIT, new Object[]{offset, length},
                new BeanPropertyRowMapper<>(Trade.class));

        return results;
    }


}
