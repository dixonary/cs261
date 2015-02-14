package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Trade;

import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TradeDao extends AbstractDao {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";

    private static final String INSERT = "INSERT INTO Trade (time, buyer, seller, price, currency, size, symbol, sector, bid, ask) VALUES (?,?,?,?,?,?,?,?,?,?);";


    public TradeDao() {


    }

    public void insertTrade(Trade ent) {
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

    public List<Trade> getTrades() {
        List<Trade> results = jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Trade.class));

        return results;
    }


}
