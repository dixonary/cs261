package util;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.NumberPath;

/**
 * Created by martin on 04/03/15.
 */
public class Filters {

    public static BooleanExpression timerangeFilter(NumberPath<Long> field, String filterString) {
        try {
            String[] parts = filterString.split(",");

            long dateFrom = Long.parseLong(parts[0]);
            long dateTo = Long.parseLong(parts[1]);

            return field.goe(dateFrom).and(field.loe(dateTo));
        } catch (Exception e) {
            return null;
        }
    }

}
