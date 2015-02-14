package team16.cs261.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import team16.cs261.model.Trade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */
public class TradeDAO  {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    JdbcTemplate template;

    public TradeDAO() {



    }

    public void createTable() throws SQLException {
        template.execute(CREATE_TABLE);
    }

    public List<Trade> getTrades() {




        return null;
    }
}
