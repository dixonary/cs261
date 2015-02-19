package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.RawComm;
import team16.cs261.dal.entity.RawTrade;
import team16.cs261.dal.entity.Trade;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class RawCommDao extends AbstractDao<Integer, RawComm> {

    private static final String INSERT = "INSERT INTO RawComm (raw) VALUES (?);";

    public RawCommDao() {
        super(RawComm.class);
    }

    @Override
    public void insert(RawComm ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getRaw()
        );
    }



}
