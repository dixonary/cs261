package team16.cs261.common.meta;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by martin on 03/03/15.
 */
public enum FactorGroup {
    NODE(null, "Node"),
        TRADER(NODE, "Trader"),
        SYMBOL(NODE, "Symbol"),
        SECTOR(NODE, "Sector"),
    EDGE(null, "Edge"),
        TRADER_TRADER(EDGE, "Trader -> Trader"),
            COMMON(TRADER_TRADER, "Common symbols"),
            COMMON_BUYS(TRADER_TRADER, "Common buys"),
            COMMON_SELLS(TRADER_TRADER, "Common sells"),
        TRADER_SYMBOL(EDGE, "Trader -> Symbol"),
        TRADER_SECTOR(EDGE, "Trader -> Sector");

    FactorGroup parent;
    String label;

    FactorGroup(FactorGroup parent, String label) {
        this.label = label;
        this.parent = parent;
    }


    public boolean isRoot() {
        return parent == null;
    }

    public String getLabel() {
        return label;
    }

    public List<FactorGroup> getChildren(FactorGroup parent) {
        List<FactorGroup> set = new ArrayList<>();

        for(FactorGroup fc : FactorGroup.values()) {
            if(fc.parent == parent) {
                set.add(fc);
            }
        }

        return set;
    }

    public EnumSet<FactorGroup> getClasses(FactorGroup cls) {
        return null;
    }
}
