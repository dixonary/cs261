package team16.cs261.dao;

import org.springframework.stereotype.Component;
import team16.cs261.entity.Symbol;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class SymbolDao extends AbstractDao {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS votes (pollId integer, voter string NOT NULL, answerIndex integer NOT NULL)";

    private static final String SELECT = "SELECT * FROM Trade";

    private static final String INSERT = "INSERT IGNORE INTO Symbol (name) VALUES (?);";



    public void insert(Symbol symbol) {
        jdbcTemplate.update(INSERT, symbol.getName());
    }


}
