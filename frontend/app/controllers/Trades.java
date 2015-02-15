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

    public Result collection() {
        return play.mvc.Controller.ok(views.html.trades.collection.render());
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Trade";
    private static final String SELECT_AND_LIMIT_COUNT = "SELECT COUNT(*) FROM Trade LIMIT ?, ?";

    public Result query() {

        int draw = Integer.parseInt(request().getQueryString("draw"));

        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));


        int recordsTotal = trades.selectCountAll();
        List<Trade> data = trades.selectAllLimit(start, length);


        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }

    public Result queryByTraderId(String email) {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        int recordsTotal = trades.countByTraderId(email);
        List<Trade> data = trades.findByTraderIdAndLimit(email, start, length);

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }
}