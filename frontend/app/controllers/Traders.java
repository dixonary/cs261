package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.dao.TraderDao;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.Trader;

import java.util.List;

import static play.mvc.Controller.*;
import static play.mvc.Results.*;

@org.springframework.stereotype.Controller
public class Traders {

    @Autowired
    TradeDao tradesDao;

    @Autowired
    TraderDao tradersDao;


    public Result element(String email) {
        Trader ent = tradersDao.selectWhereId(email);

        if (ent == null) {
            return redirect(routes.Application.traders());
        }

        List<Trade> tradeEnts = tradesDao.findByTraderId(email);

        return ok(views.html.traders.element.render(ent, tradeEnts));
        //return play.mvc.Controller.ok(Json.toJson(ent));
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Trader";
    private static final String SELECT_AND_LIMIT_COUNT = "SELECT COUNT(*) FROM Trader LIMIT ?, ?";

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        int recordsTotal = tradersDao.jdbcTemplate.queryForObject(SELECT_COUNT, Integer.class);
        List<Trader> data = tradersDao.selectAndLimit(start, length);

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }
}