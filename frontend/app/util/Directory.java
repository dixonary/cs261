package util;

import controllers.routes;
import play.api.mvc.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 08/03/15.
 */
public enum Directory implements MenuItem {
    DASHBOARD("Dashboard", "fa-dashboard", routes.Application.index()),

    CLUSTERS("Clusters", "fa-gears", routes.Application.clusters()),
    FACTORS("Factors", "fa-gear", routes.Application.factors()),
    FACTOR_CLASSES(FACTORS, "Types", "fa-info", routes.Application.factorClasses()),

    DATA("Data", "fa-database", null),

    TRADES(DATA, "Trades", "fa-exchange", routes.Application.trades()),
    COMMS(DATA, "Communications", "fa-envelope", routes.Application.comms()),
    TRADERS(DATA, "Traders", "fa-user", routes.Application.traders()),
    SYMBOLS(DATA, "Symbols", "fa-cube", routes.Application.symbols()),
    SECTORS(DATA, "Sectors", "fa-cubes", routes.Application.sectors());

    Directory parent;
    String label;
    String icon;
    Call call;

    Directory(Directory parent, String label, String icon, Call call) {
        this.parent = parent;
        this.label = label;
        this.icon = icon;
        this.call = call;
    }

    Directory(String label, String icon, Call call) {
        this(null, label, icon, call);
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public Call getCall() {
        return call;
    }

    @Override
    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public boolean isActive(String uri) {
        if (call != null && uri.equals(call.url())) return true;

        for (Directory dir : getChildren()) {
            if (dir.isActive(uri)) return true;
        }


        return false;
    }

    @Override
    public boolean hasChildren() {
        for (Directory dir : values()) {
            if (dir.parent == this) return true;
        }
        return false;
    }

    @Override
    public List<Directory> getChildren() {
        List<Directory> dirs = new ArrayList<>();

        for (Directory dir : values()) {
            if (dir.parent == this)
                dirs.add(dir);
        }
        return dirs;
    }

    public static List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>();

        for(Directory dir : values()) {
            items.add(dir);
        }

        return items;
    }

}
