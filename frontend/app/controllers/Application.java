package controllers;

import menu.Menu;
import menu.MenuItem;
import menu.Navigation;
import org.springframework.stereotype.Controller;
import play.api.mvc.Call;
import play.mvc.Result;
import views.html.index;

import java.util.LinkedHashMap;
import java.util.Map;

import static play.mvc.Results.ok;

@Controller
public class Application {

    public Result index() {
        return ok(index.render("Whoa"));
    }


    public static Map<Call, String> getNavigation() {
        Map<Call, String> map = new LinkedHashMap<>();

        map.put(controllers.routes.Application.index(), "Dashboard");
        map.put(controllers.routes.Clusters.index(), "Clusters");
        map.put(controllers.routes.Trades.index(), "Trades");

        return map;
    }

    public static Navigation getMenu() {
        MenuItem dashboard = new MenuItem("Dashboard", controllers.routes.Application.index());

        MenuItem clusters = new MenuItem("Clusters", controllers.routes.Clusters.index());

        Menu rawData = new Menu("Raw Data");
        rawData.addItem(new MenuItem("Traders", controllers.routes.Traders.index()));
        rawData.addItem(new MenuItem("Communications", controllers.routes.Comms.index()));
        rawData.addItem(new MenuItem("Trades", controllers.routes.Trades.index()));


        Navigation navigation = new Navigation("Navigation");
        navigation.addItem(dashboard);
        navigation.addItem(clusters);
        navigation.addItem(rawData);

        return navigation;
    }
}
