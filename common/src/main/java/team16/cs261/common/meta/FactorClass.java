package team16.cs261.common.meta;

import java.util.EnumSet;

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

    public FactorGroup getGroup() {
        return group;
    }

    public String getLabel() {
        return label;
    }

    public static EnumSet<FactorClass> getClasses(FactorArity arity) {
        EnumSet<FactorClass> set = EnumSet.noneOf(FactorClass.class);

        for (FactorGroup group : FactorGroup.getGroups(arity)) {
            set.addAll(getClasses(group));
        }

        return set;
    }

    public static EnumSet<FactorClass> getClasses(FactorGroup group) {
        EnumSet<FactorClass> set = EnumSet.noneOf(FactorClass.class);

        for (FactorClass cls : FactorClass.values()) {
            if (cls.getGroup() == group) {
                set.add(cls);
            }
        }

        return set;
    }


}
