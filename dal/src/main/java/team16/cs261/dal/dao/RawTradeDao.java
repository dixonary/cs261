package team16.cs261.dal.dao;

import javafx.beans.binding.Bindings;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.RawTrade;
import team16.cs261.dal.entity.Trade;

import java.util.Iterator;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class RawTradeDao extends AbstractDao<Integer, RawTrade> {

    private static final String SELECT = "SELECT * FROM Trade";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trade LIMIT ?, ?";

    private static final String INSERT = "INSERT INTO RawTrade (raw) VALUES (?);";

    private static final String DELETE_FROM = "DELETE FROM RawTrade WHERE id IN (?)";


    public RawTradeDao() {
        super(RawTrade.class);


    }

    public List<RawTrade> selectAllLimit(int length) {
        return jdbcTemplate.query(
                "SELECT * FROM RawTrade ORDER BY id ASC LIMIT ? ",
                new Object[]{length},
                new BeanPropertyRowMapper<>(RawTrade.class)
        );
    }


    @Override
    public void insert(RawTrade ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getRaw()
        );
    }






}
