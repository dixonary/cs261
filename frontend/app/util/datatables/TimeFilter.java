package util.datatables;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 07/03/15.
 */
public class TimeFilter extends Column {

    final NumberPath<Long> path;

    public TimeFilter(String name, NumberPath<Long> path) {
        super(name);
        this.path = path;
    }


    @Override
    public Predicate getPredicate(String input) {
        try {
            String[] parts = input.split(",");

            long dateFrom = Long.parseLong(parts[0]);
            long dateTo = Long.parseLong(parts[1]);

            return path.goe(dateFrom).and(path.loe(dateTo));
        } catch (Exception e) {
            return null;
        }
    }
}
