package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.SectorDao;
import util.JsonNodeRowMapper;

import java.util.List;

import static play.mvc.Controller.ok;
import static play.mvc.Controller.request;
import static play.mvc.Results.redirect;

@org.springframework.stereotype.Controller
public class Sectors {

    @Autowired
    SectorDao sectorDao;


    public Result collection() {
        return ok(views.html.sectors.collection.render());
    }

    public Result element(String sector) {
        //Trader ent = symbolDao.selectWhereId(email);

        //if (ent == null) {
            return redirect(controllers.routes.Sectors.collection());
        //}

        //List<Trade> tradeEnts = symbolDao.findByTraderId(email);

        //return ok(views.html.traders.element.render(ent, tradeEnts));
        //return play.mvc.Controller.ok(Json.toJson(ent));
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Sector S " +
            "JOIN Counter C ON S.tradeCnt = C.id";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Sector S " +
            "JOIN Counter C " +
            "ON S.tradeCnt = C.id LIMIT ?, ?";

    //SELECT * FROM Symbol S JOIN (SELECT id, avg1 as tAvg1, avg2 as tAvg2, avg3 as tAvg3 FROM Counter) AS C ON S.tradeCnt = C.id;

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        int recordsTotal = sectorDao.jdbcTemplate.queryForObject(SELECT_COUNT, Integer.class);
        //List<Symbol> data = symbolDao.selectAndLimit(start, length);
        /*List<Symbol> data = symbolDao.jdbcTemplate.query(
                SELECT_AND_LIMIT_COUNT,
                new Object[]{},
                new JsonNodeRowMapper(null));*/

        List<JsonNode> data = sectorDao.jdbcTemplate.query(
                SELECT_AND_LIMIT,
                new Object[]{start, length},
                new JsonNodeRowMapper(/*new ObjectMapper()*/));

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }

}