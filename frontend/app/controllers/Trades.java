package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.dal.dao.TradeDao;
import team16.cs261.dal.entity.Trade;

import java.util.List;

import static play.mvc.Controller.request;

@org.springframework.stereotype.Controller
public class Trades {

    @Autowired
    TradeDao trades;

    public Result index() {
        return play.mvc.Controller.ok(views.html.trades.render());
        //return play.mvc.Controller.ok(Json.toJson(trades.getTrades()));
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Trade";
    private static final String SELECT_AND_LIMIT_COUNT = "SELECT COUNT(*) FROM Trade LIMIT ?, ?";

    public Result query() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        int recordsTotal = trades.jdbcTemplate.queryForObject(SELECT_COUNT, Integer.class);

        List<Trade> data = trades.selectAndLimit(start, length);


        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }

    public Result get(int clusterId) {
        return play.mvc.Controller.ok("sup");
    }
}