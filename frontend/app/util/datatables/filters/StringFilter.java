package util.datatables.filters;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringExpression;
import util.datatables.Selection;

import java.util.Arrays;
import java.util.List;

/**
 * Created by martin on 07/03/15.
 */
public class StringFilter extends ColumnFilter {

    final List<Selection> domain;
    final StringExpression path;
    final boolean multi;

    public StringFilter(StringExpression path, List<Selection> domain, boolean multi) {
        this.path = path;
        this.domain = domain;
        this.multi = multi;
    }

    public List<Selection> getDomain() {
        return domain;
    }

    public boolean isMulti() {
        return multi;
    }

    @Override
    public Predicate getPredicate(String input) {
        if(input.equals("")) return null;

        List<String> ids = Arrays.asList(input.split(","));

        if (ids.size() == 0) {
            return null;
        }

        System.out.println("input: " + ids + " size: " + ids.size());

        return path.in(ids);
    }
}
