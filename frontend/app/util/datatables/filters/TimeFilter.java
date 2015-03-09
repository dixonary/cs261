package util.datatables.filters;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;

/**
 * Created by martin on 07/03/15.
 */
public class TimeFilter extends ColumnFilter {

    final NumberPath<Long> path;
    //final String def;

    public TimeFilter(NumberPath<Long> path, long from, long to) {
        super(false, from + "," + to);

        this.path = path;
    }

    public TimeFilter(NumberPath<Long> path) {
        this(path, 0, 0);
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
