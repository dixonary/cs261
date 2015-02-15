package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Symbol;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class SymbolDao extends AbstractDao<Integer, Symbol> {

    private static final String INSERT = "INSERT IGNORE INTO Symbol (name) VALUES (?);";

    public SymbolDao() {
        super(Symbol.class);
    }

    public void insert(Symbol symbol) {
        jdbcTemplate.update(INSERT, symbol.getName());
    }


}
