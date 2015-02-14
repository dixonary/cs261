package team16.cs261.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import team16.cs261.model.Trade;
import team16.cs261.model.Trader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */
public class TraderDAO extends JdbcTemplate {

    private static final String CREATE_TABLE = "CREATE TABLE Trader " +
            "(email varchar(50) NOT NULL, domain varchar(30) NOT NULL" + ");";
            //"avg_1 integer NOT NULL,Avg_2 integer NOT NULL,Avg_3 integer NOT NULL,PRIMARY KEY(Email));";

    private static final String INSERT_TRADER = "INSERT INTO Trader (email, domain)  VALUES (?, ?)";
    //private static final String


    public TraderDAO() {


    }

    public void createTable() throws SQLException {
        //execute(CREATE_TABLE);
    }


    public void insertTrader(Trader trader) {
        update(INSERT_TRADER, trader.getEmail(), trader.getDomain());
    }

    public List<Trade> getTrades() {


        List<Trade> results = null; /*jdbcTemplate.query(
                "select * from customers where first_name = ?", new Object[] { "Josh" },
                new RowMapper<Trade>() {
                    @Override
                    public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Trade(rs.getLong("id"), rs.getString("first_name"),
                                rs.getString("last_name"));
                    }
                });*/

        return results;
    }
}
