package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Comm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class CommDao extends AbstractDao<Comm> {

    private static final String SELECT = "SELECT * FROM Comm";

    private static final String INSERT = "INSERT INTO Comm (time, sender, recipient)  VALUES (?, ?, ?)";


    public CommDao() {


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

       /* jdbcTemplate.batchUpdate(INSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Comm ent = ents.get(i);
                ps.setLong(1, ent.getTimestamp());
                ps.setString(2, ent.getSender());
                ps.setString(3, ent.getRecipient());
            }

            @Override
            public int getBatchSize() {
                return ents.size();
            }
        });*/


    }

    @Override
    public List<Comm> findAll() {
        List<Comm> results = jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Comm.class));

        return results;
    }

    @Override
    public Comm findById(int id) {
        return null;
    }

}
