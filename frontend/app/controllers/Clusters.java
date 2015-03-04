package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import models.ClusterDto;
import models.FactorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.entity.Cluster;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.factor.Factor;
import team16.cs261.common.querydsl.entity.QCluster;
import team16.cs261.common.querydsl.entity.QFactor;
import team16.cs261.common.querydsl.entity.QTick;
import util.Filters;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.*;

@org.springframework.stereotype.Controller
public class Clusters {

    @Autowired
    QueryDslJdbcTemplate template;

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TradeDao tradeDao;

    public Result collection() {
        return ok(views.html.clusters.collection.render());
    }

    public Result element(int clusterId) {
        Cluster clusterEnt = clusterDao.selectWhereId(clusterId);
        List<Factor> factorEnts = factorDao.findByClusterId(clusterId);

        if (clusterEnt == null) {
            return redirect(controllers.routes.Clusters.collection());
        }

        return ok(views.html.clusters.element.render(clusterEnt, factorEnts));
    }

    private final QCluster c = new QCluster("c");
    private final QTick t = new QTick("t");
    private final ComparableExpressionBase[] columns = {
            //f.id, t.start, f.factor, null, f.value, f.centile, f.sig
            c.id, c.tick
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


        String daterange = request().getQueryString("columns[0][search][value]");
        System.out.println("Daterange: " + daterange);

        //int recordsTotal = clusterDao.selectCountAll();
        //int recordsFiltered = recordsTotal;

/*        List<Cluster> data;
        try {
            long dateFrom = Long.parseLong(daterange.split(",")[0]);
            long dateTo = Long.parseLong(daterange.split(",")[1]);

            recordsFiltered = clusterDao.selectCountWhereDaterange(dateFrom, dateTo);

            data = clusterDao.selectWhereDaterange(dateFrom, dateTo, start, length);
        } catch (Exception e) {

            data = clusterDao.selectAllLimit(start, length);
        }*/




        SQLQuery query = template.newSqlQuery().from(c).join(t).on(c.tick.eq(t.tick))
                .where(fbt)
                .orderBy(order).offset(start).limit(length);

        long recordsTotal = template.count(template.newSqlQuery().from(c));
        long recordsFiltered = template.count(query);


        List<ClusterDto> data = template.query(query, Projections.constructor(
                //FactorDto.class, f.id, f.tick, t.start, f.factor, f.value, f.centile, f.sig
                ClusterDto.class, c.id, c.tick, t.start
        ));





/*        List<ClusterDto> dtos = new ArrayList<>();
        for (Cluster clu : data) {
            List<Factor> factorList = factorDao.findByClusterId(clu.getClusterId());
            dtos.add(new ClusterDto(clu, factorList));
        }*/

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsFiltered);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }

   /* public Result query() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        String daterange = request().getQueryString("columns[0][search][value]");
        System.out.println("Daterange: " + daterange);

        int recordsTotal = clusterDao.selectCountAll();
        int recordsFiltered = recordsTotal;

        List<Cluster> data;
        try {
            long dateFrom = Long.parseLong(daterange.split(",")[0]);
            long dateTo = Long.parseLong(daterange.split(",")[1]);

             recordsFiltered = clusterDao.selectCountWhereDaterange(dateFrom, dateTo);

            data = clusterDao.selectWhereDaterange(dateFrom, dateTo, start, length);
        } catch (Exception e) {

            data = clusterDao.selectAllLimit(start, length);
        }





        List<ClusterDto> dtos = new ArrayList<>();
        for (Cluster clu : data) {
            List<Factor> factorList = factorDao.findByClusterId(clu.getClusterId());
            dtos.add(new ClusterDto(clu, factorList));
        }

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsFiltered);
        response.put("data", Json.toJson(dtos));
        return play.mvc.Controller.ok(response);
    }*/
}