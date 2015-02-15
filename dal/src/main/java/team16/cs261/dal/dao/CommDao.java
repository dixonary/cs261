package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Cluster;
import team16.cs261.dal.entity.Comm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class CommDao extends AbstractDao<Integer, Comm> {

    private static final String INSERT = "INSERT INTO Comm (time, sender, recipient)  VALUES (?, ?, ?)";
    private static final String SELECT = "SELECT * FROM Comm";

    public CommDao() {
        super(Comm.class);
    }

    @Override
    public void insert(Comm ent) {
        jdbcTemplate.update(INSERT, ent.getTimestamp(), ent.getSender(), ent.getRecipient());
    }

    @Override
    public void insert(final Iterable<Comm> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Comm ent : ents) {
            args.add(new Object[]{ent.getTimestamp(), ent.getSender(), ent.getRecipient()});
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
