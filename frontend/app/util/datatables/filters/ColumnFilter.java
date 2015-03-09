package util.datatables.filters;

import com.mysema.query.types.Predicate;

/**
 * Created by martin on 06/03/15.
 */
public abstract class ColumnFilter {

    final boolean multi;
    final Object value;

    public ColumnFilter() {
        this(false, null);
    }

    public ColumnFilter(Object value) {
        this(false, value);
    }

    protected ColumnFilter(boolean multi, Object value) {
        this.multi = multi;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isMulti() {
        return multi;
    }

    public abstract Predicate getPredicate(String input) ;
}
