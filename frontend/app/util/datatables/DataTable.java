package util.datatables;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import play.libs.Json;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Controller.response;
import static play.mvc.Results.ok;

/**
 * Created by martin on 05/03/15.
 */
public abstract class DataTable<E> {

    @Autowired
    public QueryDslJdbcTemplate template;

    public abstract Column[] getColumnDefs();

    public abstract SQLQuery getQuery();

    public abstract OrderSpecifier getOrder();


    public Predicate getViewPredicate() {
        return null;
    }

    public abstract Predicate getPredicate();

    public abstract Expression<E> getProjection();

    public abstract String getSource();

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        OrderSpecifier order = getOrder();
        Predicate where = getPredicate();

        SQLQuery total = getQuery();
        SQLQuery filtered = getQuery()
                .where(where)
                .orderBy(order)
                .offset(start).limit(length);

        long recordsTotal = template.count(total);
        long recordsFiltered = template.count(filtered);
        List<E> data = template.query(filtered, getProjection());

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsFiltered);
        response.put("data", Json.toJson(data));
        return ok(response);
    }

    public Result csv() {
        OrderSpecifier order = getOrder();
        Predicate where = getPredicate();

        SQLQuery filtered = getQuery()
                .where(where)
                .orderBy(order);

        List<String> lines = template.query(filtered, getCsvProjection());

        StringBuilder csv = new StringBuilder()
                .append(getCsvHeader()).append("\n");
        for (String line : lines) {
            csv.append(line).append("\n");
        }


        response().setContentType("text/csv");
        return ok(csv.toString());
    }

    ObjectNode meta;

    public Result meta() {
        if (meta == null) {
            meta = Json.newObject();

            meta.put("source", getSource());
            meta.put("columns", Json.toJson(getColumnDefs()));
        }
        return ok(meta);
    }

    public String getCsvHeader() {
        return null;
    }

    public List<StringExpression> getCsvColumns() {
        return new ArrayList<>();
    }

    public Expression<String> getCsvProjection() {
        Iterator<StringExpression> it = getCsvColumns().iterator();

        StringExpression expr = it.next();

        while (it.hasNext()) {
            expr = expr.concat(",").concat(it.next());
        }

        return expr;
    }
}
