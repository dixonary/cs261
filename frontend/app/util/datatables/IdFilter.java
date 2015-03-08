package util.datatables;

import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 07/03/15.
 */
public class IdFilter extends Column {

    final List<DomainValue> domain;
    final NumberPath<Integer> path;
    final boolean multi;

    public IdFilter(String name, NumberPath<Integer> path, List<DomainValue> domain, boolean multi) {
        super(name);

        this.path = path;
        this.domain = domain;
        this.multi = multi;
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
