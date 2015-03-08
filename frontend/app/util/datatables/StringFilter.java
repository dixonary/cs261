package util.datatables;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.StringPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 07/03/15.
 */
public class StringFilter extends Column {

    final List<DomainValue<String>> domain;
    final StringPath path;
    final boolean multi;

    public StringFilter(String name, StringPath path, List<DomainValue<String>> domain, boolean multi) {
        super(name);

        this.path = path;
        this.domain = domain;
        this.multi = multi;
    }

    public List<DomainValue<String>> getDomain() {
        return domain;
    }

    public boolean isMulti() {
        return multi;
    }

    @Override
    public Predicate getPredicate(String input) {
        List<String> ids = new ArrayList<>();

        //if(ids.size()==0)return null;

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
