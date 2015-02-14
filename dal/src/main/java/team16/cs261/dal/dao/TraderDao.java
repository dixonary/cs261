package team16.cs261.dal.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Trader;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class TraderDao extends AbstractDao {


    private static final String CREATE_TABLE = "CREATE TABLE Trader " +
            "(email varchar(50) NOT NULL, domain varchar(30) NOT NULL" + ");";
    //"avg_1 integer NOT NULL,Avg_2 integer NOT NULL,Avg_3 integer NOT NULL,PRIMARY KEY(Email));";

    private static final String INSERT = "INSERT IGNORE INTO Trader (email, domain)  VALUES (?, ?)";

    private static final String SELECT = "SELECT * FROM Trader";

    //private static final String


    public TraderDao() {


    }

    public void createTable() throws SQLException {
        //execute(CREATE_TABLE);
    }


    public void insertTrader(Trader trader) {
        jdbcTemplate.update(INSERT, trader.getEmail(), trader.getDomain());
    }

    public void insertTraders(final List<Trader> ents) {
        jdbcTemplate.batchUpdate(INSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Trader ent = ents.get(i);
                ps.setString(1, ent.getEmail());
                ps.setString(2, ent.getDomain());
            }

            @Override
            public int getBatchSize() {
                return ents.size();
            }
        });


    }

    public List<Trader> getTraders() {
        List<Trader> results = jdbcTemplate.query(
                SELECT, new Object[0],
                new BeanPropertyRowMapper<>(Trader.class));

        return results;
    }
}
