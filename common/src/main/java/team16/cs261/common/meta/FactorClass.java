package team16.cs261.common.meta;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by martin on 03/03/15.
 */
public enum FactorClass {
    COMMON(FactorGroup.TRADER_TRADER, "Common symbols", 0.05D, "", ""),
    COMMON_BUYS(FactorGroup.TRADER_TRADER, "Common buys", 0.25D, "", ""),
    COMMON_SELLS(FactorGroup.TRADER_TRADER, "Common sells", 0.25D, "", ""),
    COMMS(FactorGroup.TRADER_TRADER, "Comms", 0.5D,  "", ""),

    TRADES(FactorGroup.TRADER_SYMBOL, "Trades", 0.05D, "", ""),
    BUYS(FactorGroup.TRADER_SYMBOL, "Buys", 0.05D, "", ""),
    SELLS(FactorGroup.TRADER_SYMBOL, "Sells", 0.05D, "", "");


    FactorGroup group;
    String label;
    public double sig;

    String lambda;

    FactorClass(FactorGroup group, String label, double sig, String lambda, String field) {
        this.group = group;
        this.label = label;
        this.sig=sig;
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

    public static List<FactorClass> getImplemented() {
        return Arrays.asList(COMMON, COMMON_BUYS, COMMON_SELLS, COMMS);
    }



}
