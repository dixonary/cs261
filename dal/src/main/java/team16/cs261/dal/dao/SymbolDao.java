package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class SymbolDao extends AbstractDao<Integer, Symbol> {

    //private static final String INSERT = "INSERT IGNORE INTO Symbol (name, sector) VALUES (?, ?);";
    private static final String INSERT = "Call InsertSymbol (?, ?);";

    //private static final String INSERT = "INSERT INTO Counter VALUES (), ()" +
//            "INSERT IGNORE INTO Symbol (name, sector, tradeCnt, priceCnt) VALUES (?, ?, LAST_INSERT_ID(), LAST_INSERT_ID()+1);";





    public SymbolDao() {
        super(Symbol.class);
    }

    public void insert(Symbol symbol) {
        jdbcTemplate.update(INSERT, symbol.getSymbol(), symbol.getSector());
    }

    public void insert(final Iterable<Symbol> ents) {
        List<Object[]> args = new ArrayList<>();
        for (Symbol ent : ents) {
            args.add(new Object[]{ent.getSymbol(), ent.getSector()});
        }

        jdbcTemplate.batchUpdate(INSERT, args);
    }


}
