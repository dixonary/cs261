package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.CommDao;
import team16.cs261.common.dao.TraderDao;
import team16.cs261.common.entity.Comm;
import util.JsonNodeRowMapper;

import java.util.List;

import static play.mvc.Controller.request;

@Controller
public class Comms {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommDao comms;

    public Result collection() {
        return play.mvc.Controller.ok(views.html.comms.collection.render());
    }

    public Result get(int clusterId) {
        return play.mvc.Controller.ok("sup");
    }

    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM Comm C";
    private static final String SELECT_AND_LIMIT = "SELECT * FROM Comm C " +
            "LIMIT ?, ?";

    //SELECT * FROM Symbol S JOIN (SELECT id, avg1 as tAvg1, avg2 as tAvg2, avg3 as tAvg3 FROM Counter) AS C ON S.tradeCnt = C.id;

    public Result query() {
        int draw = Integer.parseInt(request().getQueryString("draw"));
        int start = Integer.parseInt(request().getQueryString("start"));
        int length = Integer.parseInt(request().getQueryString("length"));

        int recordsTotal = jdbcTemplate.queryForObject(SELECT_COUNT, Integer.class);

        //List<Comm> data = comms.selectAllLimit(start, length);
        List<JsonNode> data = jdbcTemplate.query(
                SELECT_AND_LIMIT,
                new Object[]{start, length},
                new JsonNodeRowMapper());

        ObjectNode response = Json.newObject();
        response.put("draw", draw);
        response.put("recordsTotal", recordsTotal);
        response.put("recordsFiltered", recordsTotal);
        response.put("data", Json.toJson(data));
        return play.mvc.Controller.ok(response);
    }
}