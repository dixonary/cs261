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
        map.put(controllers.routes.Clusters.collection(), "Clusters");
        map.put(controllers.routes.Trades.collection(), "Trades");

        return map;
    }

    public static Navigation getMenu() {
        MenuItem dashboard = new MenuItem("fa-dashboard", "Dashboard", controllers.routes.Application.index());

        MenuItem clusters = new MenuItem("fa-cubes", "Clusters", controllers.routes.Clusters.collection());
        MenuItem factors = new MenuItem("fa-cube", "Factors", controllers.routes.Clusters.collection());

        Menu rawData = new Menu("fa-database", "Raw Data");
        rawData.addItem(new MenuItem("fa-exchange", "Trades", controllers.routes.Trades.collection()));
        rawData.addItem(new MenuItem("fa-envelope", "Communications", controllers.routes.Comms.collection()));
        rawData.addItem(new MenuItem("fa-user", "Traders",
                controllers.routes.Traders.collection(),
                new Call[]{
                        controllers.routes.Traders.element("test")
                }
        ));


        Navigation navigation = new Navigation("Navigation");
        navigation.addItem(dashboard);
        navigation.addItem(clusters);
        navigation.addItem(factors);
        navigation.addItem(rawData);

        return navigation;
    }
}
