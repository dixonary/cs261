package util.datatables.filters;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;
import util.datatables.Selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 07/03/15.
 */
public class IdFilter extends ColumnFilter {

    final List<Selection> domain;
    final NumberPath<Integer> path;

    public IdFilter(NumberPath<Integer> path, List<Selection> domain, boolean multi) {
        super(multi, null);
        this.path = path;
        this.domain = domain;
    }

    public List<Selection> getDomain() {
        return domain;
    }

    public boolean isMulti() {
        return multi;
    }

    @Override
    public Predicate getPredicate(String input) {
        List<Integer> ids = getIds(input);

        if (ids.size() == 0) return null;

        return path.in(ids);
    }

    public static List<Integer> getIds(String input) {
        if (input == null) return Collections.EMPTY_LIST;

        List<Integer> ids = new ArrayList<>();

        for (String part : input.split(",")) {
            try {
                ids.add(Integer.parseInt(part));
            } catch (Exception ignored) {

            }
        }

        return ids;
    }
}
