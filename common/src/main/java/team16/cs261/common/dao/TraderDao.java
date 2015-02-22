package team16.cs261.common.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.common.entity.Trader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TraderDao extends AbstractDao<String, Trader> {

    //private static final String INSERT = "INSERT IGNORE INTO Trader (email, domain)  VALUES (?, ?)";
    private static final String INSERT = "CALL InsertTrader(?, ?)";

    private static final String SELECT = "SELECT * FROM Trader";
    private static final String SELECT_BY_ID = "SELECT * FROM Trader WHERE email = ?";

    private static final String SELECT_AND_LIMIT = "SELECT * FROM Trader LIMIT ?, ?";


    //private static final String


    public TraderDao() {
        super(Trader.class);

    }

    public void createTable() throws SQLException {
        //execute(CREATE_TABLE);
    }

    public void insert(Trader trader) {
        jdbcTemplate.update(INSERT, trader.getEmail(), trader.getDomain());
    }

    public void insert(final Iterable<Trader> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Trader ent : ents) {
            args.add(new Object[]{ent.getEmail(), ent.getDomain()});
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }

/*    @Override
    public List<Trader> selectAll() {
        return jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Trader.class));
    }*/

/*    @Override
    public Trader selectWhereId(String id) {
        return jdbcTemplate.queryForObject(
                SELECT_BY_ID, new Object[]{id},
                new BeanPropertyRowMapper<>(Trader.class));
    }*/


    public List<Trader> selectAndLimit(int offset, int length) {
        return jdbcTemplate.query(
                SELECT_AND_LIMIT, new Object[]{offset, length},
                new BeanPropertyRowMapper<>(Trader.class));
    }
}
