package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Result;
import team16.cs261.dal.dao.ClusterDao;
import team16.cs261.dal.dao.FactorDao;
import team16.cs261.dal.dao.TradeDao;

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
        return ok(views.html.clusters.collection.render());
    }


}