package controllers;

import org.springframework.stereotype.Controller;
import play.Routes;
import play.mvc.Result;
import scala.Option;
import scala.Some;
import views.html.index;


import static play.mvc.Controller.response;
import static play.mvc.Results.ok;

@Controller
public class Application {



    public Result index() {
        return ok(index.render("Whoa"));
    }

    public Result clusters() {
        return ok(views.html.clusters.collection.render());
    }

    public Result factors() {
        return ok(views.html.factors.collection.render());
    }

    public Result factorClasses() {
        return ok(views.html.factors.info.render());
    }

    public Result trades() {
        return ok(views.html.trades.collection.render());
    }

    public Result comms() {
        return ok(views.html.comms.collection.render());
    }

    public Result tradesBy(String ids1, String ids2, String ids3, String ids4) {
        return ok(views.html.trades.collection.render());
    }

    public Result commsBy(String ids1, String ids2) {
        return ok(views.html.comms.collection.render());
    }







    public Result traders() {
        return ok(views.html.traders.collection.render());
    }


    public Result symbols() {
        return ok(views.html.symbols.collection.render());
    }


    public Result sectors() {
        return ok(views.html.sectors.collection.render());
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        //Routes.

        return ok(
                Routes.javascriptRouter("jsRoutes",
                        //routes.javascript.Application.tradesByBuyer(),
                        //routes.javascript.Application.tradesBySeller(),
                        //routes.javascript.Application.tradesByTraders(),
                        routes.javascript.Application.tradesBy(),
                        //routes.javascript.Application.commsBySender(),
                        //routes.javascript.Application.commsByRecipient(),
                        routes.javascript.Application.commsBy(),
                        routes.javascript.Clusters.element(),
                        controllers.data.routes.javascript.Dashboard.rates(),
                        controllers.data.routes.javascript.Dashboard.freqs()
                )
        );
    }

}
