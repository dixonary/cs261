package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.dal.dao.ClusterDao;
import team16.cs261.dal.dao.FactorDao;
import team16.cs261.dal.dao.TradeDao;
import team16.cs261.dal.entity.factor.Factor;

import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Factors {

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TradeDao tradeDao;

    public Result collection() {
        return ok(views.html.factors.collection.render());
    }

    public Result query() {

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