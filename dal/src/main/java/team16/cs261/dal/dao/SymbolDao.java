package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Symbol;
import team16.cs261.dal.entity.Trader;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void insert(final Iterable<Symbol> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Symbol ent : ents) {
            args.add(new Object[]{ent.getName()});
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }


}
