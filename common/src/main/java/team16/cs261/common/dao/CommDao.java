package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Comm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class CommDao extends AbstractDao<Integer, Comm> {

    private static final String INSERT = "CALL InsertComm(?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM Comm";

    public CommDao() {
        super(Comm.class);
    }

    public void insert(Comm ent) {
        jdbcTemplate.update(INSERT,
                ent.getTime(), ent.getTick(),
                ent.getSender(), ent.getRecipient()
        );
    }

    public void insert(final Iterable<Comm> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Comm ent : ents) {
            args.add(new Object[]{
                    ent.getTime(), ent.getTick(),
                    ent.getSender(), ent.getRecipient()
            });
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }

    @Override
    public List<Comm> selectAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Comm.class));
    }

    @Override
    public Comm selectWhereId(Integer id) {
        return null;
    }

}
