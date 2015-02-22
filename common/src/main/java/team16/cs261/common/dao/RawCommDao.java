package team16.cs261.common.dao;

import org.springframework.stereotype.Component;
import team16.cs261.common.entity.RawComm;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class RawCommDao extends AbstractDao<Integer, RawComm> {

    private static final String INSERT = "INSERT INTO RawComm (raw) VALUES (?);";

    public RawCommDao() {
        super(RawComm.class);
    }

    public void insert(RawComm ent) {
        jdbcTemplate.update(
                INSERT,
                ent.getRaw()
        );
    }

}
