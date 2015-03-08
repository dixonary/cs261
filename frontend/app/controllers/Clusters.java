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


    private final QCluster c = new QCluster("c");
    private final QTick t = new QTick("t");
    private final ComparableExpressionBase[] columns = {
            //f.id, t.start, f.factor, null, f.value, f.centile, f.sig
            c.id, c.tick
    };

    public Result element(int id) {


        SQLQuery select = template.newSqlQuery().
                from(c).
                join(t).
                on(c.tick.eq(t.tick)).
                where(c.id.eq(id));


        ClusterDto dto = template.queryForObject(select, Projections.constructor(
                ClusterDto.class, c.id, c.tick, t.start, t.end
        ));


        if (dto == null) {
            return redirect(routes.Application.clusters());
        }




        //return ok(views.html.clusters.element.render(clusterEnt, factorEnts));
        //return ok(views.html.clusters.element.render(dto.get(0), "/data/graph/"+dto.get(0).getTick()));
        //return ok(views.html.clusters.element.render());
        //return ok(Json.toJson(dto));
        ///data/graph/clusters/:tick
        return ok(views.html.pages.cluster.render(id, "/data/graph/clusters/"+dto.getTick()+"?cluster="+dto.getId()));
    }

    public Result data(int id) {


        SQLQuery select = template.newSqlQuery().
                from(c).
                join(t).
                on(c.tick.eq(t.tick)).
                where(c.id.eq(id));



        /*List<ClusterDto> dto = template.query(select, Projections.constructor(
                ClusterDto.class, c.id, c.tick, t.start, t.end
        ));*/

        ClusterDto dto = template.queryForObject(select, Projections.constructor(
                ClusterDto.class, c.id, c.tick, t.start, t.end
        ));


        /*if (dto.size() == 0) {
            return null;
        }*/




        //return ok(views.html.clusters.element.render(clusterEnt, factorEnts));
        //return ok(views.html.clusters.element.render(dto.get(0), "/data/graph/"+dto.get(0).getTick()));
        return ok(Json.toJson(dto));
    }


    public Result meta() {
        ObjectNode node = Json.newObject();


        return ok(node);
    }
}