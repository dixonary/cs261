package util.datatables;

import util.datatables.filters.ColumnFilter;

/**
 * Created by martin on 06/03/15.
 */
public class Column {

    final String name;
    final ColumnFilter filter;

    public Column(String name, ColumnFilter filter) {
        this.name = name;
        this.filter = filter;
    }

    public Column(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public ColumnFilter getFilter() {
        return filter;
    }
}
