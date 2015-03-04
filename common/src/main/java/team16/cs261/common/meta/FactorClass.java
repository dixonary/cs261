package team16.cs261.common.meta;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by martin on 03/03/15.
 */
public enum FactorClass {
    COMMON(FactorGroup.TRADER_TRADER, "Common symbols", "", ""),
    COMMON_BUYS(FactorGroup.TRADER_TRADER, "Common buys", "", ""),
    COMMON_SELLS(FactorGroup.TRADER_TRADER, "Common sells", "", ""),
    COMMS(FactorGroup.TRADER_TRADER, "Comms", "", ""),

    TRADES(FactorGroup.TRADER_SYMBOL, "Trades", "", ""),
    BUYS(FactorGroup.TRADER_SYMBOL, "Buys", "", ""),
    SELLS(FactorGroup.TRADER_SYMBOL, "Sells", "", "");


    FactorGroup group;
    String label;

    String lambda;

    FactorClass(FactorGroup group, String label, String lambda, String field) {
        this.group = group;
        this.label = label;
        this.lambda = lambda;
    }


    public String getLabel() {
        return label;
    }

    public List<FactorClass> getChildren(FactorClass parent) {
        List<FactorClass> set = new ArrayList<>();

        for (FactorClass fc : FactorClass.values()) {
            /*if(fc.parent == parent) {
                set.add(fc);
            }*/
        }

        return set;
    }

    public EnumSet<FactorClass> getClasses(FactorClass cls) {
        return null;
    }

    enum Arity {
        NODE,
        EDGE
    }

    enum Relation {
        TRADER(Arity.NODE),
        SYMBOL(Arity.NODE),
        SECTOR(Arity.NODE),
        TRADER_TRADER(Arity.EDGE),
        TRADER_SYMBOL(Arity.EDGE),
        TRADER_SECTOR(Arity.EDGE);

        Arity arity;

        Relation(Arity arity) {
            this.arity = arity;
        }
    }

}
