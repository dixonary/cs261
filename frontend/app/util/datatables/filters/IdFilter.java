package util.datatables.filters;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;
import util.datatables.DomainValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 07/03/15.
 */
public class IdFilter extends ColumnFilter {

    final List<DomainValue> domain;
    final NumberPath<Integer> path;

    public IdFilter(NumberPath<Integer> path, List<DomainValue> domain, boolean multi) {
        super(multi, null);
        this.path = path;
        this.domain = domain;
    }

    public List<DomainValue> getDomain() {
        return domain;
    }

    public boolean isMulti() {
        return multi;
    }

    @Override
    public Predicate getPredicate(String input) {
        List<Integer> ids = getIds(input);

        if(ids.size()==0)return null;

        return path.in(ids);
    }

    public static List<Integer> getIds(String input) {
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
