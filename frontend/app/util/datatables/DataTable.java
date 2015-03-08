package util.datatables;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import play.libs.Json;
import play.mvc.Result;

import java.util.List;

import static play.mvc.Controller.request;
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


    public Predicate getViewPredicate() {return null;}

    public abstract Predicate getPredicate();

    public abstract ConstructorExpression<E> getProjection();

    public abstract String getSource() ;

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        //ordering
        OrderSpecifier order = getOrder();

        //filtering
        //BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));
        //BooleanExpression fcf = factorClassFilter(request().getQueryString("columns[2][search][value]"));
        Predicate where = getPredicate();

        SQLQuery total = getQuery();
        SQLQuery filtered = getQuery()
                .where(where)
                .orderBy(order)
                .offset(start).limit(length);

        long recordsTotal = template.count(total);
        long recordsFiltered = template.count(filtered);

        List<E> data = template.query(filtered, getProjection());
/*        List<FactorDto> data = template.query(query, Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start, f.factor, f.value, f.centile, f.sig
        ));*/

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsFiltered);
        response.put("data", Json.toJson(data));
        return ok(response);
    }


    public Result meta() {
        ObjectNode meta = Json.newObject();

        meta.put("source", getSource());

        meta.put("columns", Json.toJson(getColumnDefs()));

        return ok(meta);
    }

}
