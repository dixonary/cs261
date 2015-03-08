package util.datatables;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.StringExpression;
import oracle.jrockit.jfr.events.ContentTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.querydsl.entity.QTrade;
import team16.cs261.common.querydsl.entity.Trade;

import java.util.*;

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

    public abstract ConstructorExpression<E> getProjection();

    public abstract String getSource();

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

    public Result csv() {
        //ordering
        OrderSpecifier order = getOrder();

        //filtering
        //BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));
        //BooleanExpression fcf = factorClassFilter(request().getQueryString("columns[2][search][value]"));
        Predicate where = getPredicate();

        SQLQuery filtered = getQuery()
                .where(where)
                .orderBy(order);

        long recordsFiltered = template.count(filtered);


        //List<E> data = template.query(filtered, getProjection());

/*        QTrade t = QTrade.Trade;

        List<Trade> data = template.query(filtered, Projections.bean(
                Trade.class, t.time, t.buyer, t.seller
        ));*/

/*        List<FactorDto> data = template.query(query, Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start, f.factor, f.value, f.centile, f.sig
        ));*/

        List<String> lines = template.query(filtered, getCsvProjection());

        StringBuilder csv = new StringBuilder();

        csv.append(getCsvHeader()).append("\n");
        /*for(E line : data) {
            //csv.append(toCsv(line));
            csv.append("\n");
        }*/

        for (String line : lines) {
            csv.append(line).append("\n");
        }

        System.out.println("csv: " + csv.toString());

        response().setContentType("text/csv");

        return ok(csv.toString());
    }


    public Result meta() {
        ObjectNode meta = Json.newObject();

        meta.put("source", getSource());

        meta.put("columns", Json.toJson(getColumnDefs()));

        return ok(meta);
    }

    public String getCsvHeader() {
        return "headers";
    }

    public List<StringExpression> getCsvFields() {
        return new ArrayList<>();
    }

    public Expression<String> getCsvProjection() {
        Iterator<StringExpression> it = getCsvFields().iterator();

        StringExpression expr = it.next();

        while (it.hasNext()) {
            expr = expr.concat(",").concat(it.next());
        }

        return expr;
    }
}
