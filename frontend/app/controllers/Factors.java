package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import models.FactorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.entity.factor.Factor;
import team16.cs261.common.meta.FactorClass;
import team16.cs261.common.meta.FactorGroup;
import team16.cs261.common.querydsl.entity.QFactor;
import team16.cs261.common.querydsl.entity.QTick;
import util.FactorClassUtil;
import util.Filters;

import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Factors {


    @Autowired
    QueryDslJdbcTemplate template;

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TradeDao tradeDao;

    public Result collection() {
        return ok(views.html.factors.collection.render());
    }

    private final QFactor f = new QFactor("f");
    private final QTick t = new QTick("t");
    private final ComparableExpressionBase[] columns = {
            f.id, t.start, f.factor, null, f.value, f.centile, f.sig
    };

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        //ordering
        int orderColumn = Integer.parseInt(request().getQueryString("order[0][column]"));
        boolean asc = "asc".equals(request().getQueryString("order[0][dir]"));
        OrderSpecifier order = asc ? columns[orderColumn].asc() : columns[orderColumn].desc();

        //filtering
        BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));
        BooleanExpression fcf = factorClassFilter(request().getQueryString("columns[2][search][value]"));


        //int recordsTotal = factorDao.selectCountAll();
        //List<Factor> data = factorDao.selectAllLimit(start, length);

        SQLQuery query = template.newSqlQuery().from(f).join(t).on(f.tick.eq(t.tick))
                .where(fbt, fcf)
                .orderBy(order).offset(start).limit(length);

        long recordsTotal = template.count(template.newSqlQuery().from(f));
        long recordsFiltered = template.count(query);

        List<FactorDto> data = template.query(query, Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start, f.factor, f.value, f.centile, f.sig
        ));

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsFiltered);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }

    public BooleanExpression factorClassFilter(String filterString) {
        EnumSet<FactorClass> classes = FactorClassUtil.getClasses(filterString);

        if (classes == null) return null;

        List<String> names = new ArrayList<>();
        for (FactorClass fc : classes) {
            names.add(fc.name());
        }

        return f.factor.in(names);
    }


    public Result meta() {
        ObjectNode meta = Json.newObject();
        ObjectNode filters = Json.newObject();

        meta.put("filters", filters);

        filters.put("factors", Json.toJson(FactorClassUtil.getFactorTree()));

        return ok(meta);
    }


    public Result query2() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        int recordsTotal = factorDao.selectCountAll();
        List<Factor> data = factorDao.selectAllLimit(start, length);


        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }


}