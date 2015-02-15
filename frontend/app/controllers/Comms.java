package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.Result;
import team16.cs261.dal.dao.TraderDao;

@org.springframework.stereotype.Controller
public class Comms {

    @Autowired
    TraderDao traders;

    public Result collection() {
        return play.mvc.Controller.ok(views.html.trades.collection.render());
    }

    public Result get(int clusterId) {
        return play.mvc.Controller.ok("sup");
    }
}