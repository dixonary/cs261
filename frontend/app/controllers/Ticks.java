package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.graph.EdgeDto;
import models.graph.NodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.TickDao;
import team16.cs261.common.entity.Tick;

import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

@org.springframework.stereotype.Controller
public class Ticks {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TickDao ticks;

    public Result collection() {
        return ok(views.html.trades.collection.render());
    }

    public Result element(int id) {
        Tick tick = ticks.selectWhereId(id);

        if (tick == null) {
            return redirect(controllers.routes.Application.index());
        }

        return ok(views.html.ticks.element.render(tick, "/data/graph/factors/"+tick.getTick()));
    }




    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Tick";
    private static final String SELECT_AND_LIMIT_COUNT = "SELECT COUNT(*) FROM Tick LIMIT ?, ?";

    public Result query() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        int recordsTotal = ticks.selectCountAll();
        List<Tick> data = ticks.selectAllLimit(start, length);


        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return ok(response);
    }


}