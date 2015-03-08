package util.datatables;

import com.mysema.query.types.Predicate;

/**
 * Created by martin on 06/03/15.
 */
public class Column {

    final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Predicate getPredicate(String input) {
        return null;
    }
}
