package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.RawTrade;
import team16.cs261.dal.entity.Trade;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class RawTradeDao extends AbstractDao<Integer, RawTrade> {

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    private static final String INSERT = "INSERT INTO RawTrade (raw) VALUES (?);";


    public RawTradeDao() {
        super(RawTrade.class);
    }



    @Override
    public void insert(RawTrade ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getRaw()
        );
    }



}
