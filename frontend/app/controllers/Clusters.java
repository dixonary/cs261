package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ClusterDto;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.entity.Cluster;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.factor.Factor;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.*;

@org.springframework.stereotype.Controller
public class Clusters {

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

    public Result query() {

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
    }
}