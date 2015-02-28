package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.common.dao.ClusterDao;
import team16.cs261.common.dao.FactorDao;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.TradeDao;
import team16.cs261.common.entity.factor.Factor;

import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

@org.springframework.stereotype.Controller
public class Stats {

    @Autowired
    PropDao propDao;

    public Result index() {
        return ok(views.html.stats.render(propDao.getProperties()));
    }

}