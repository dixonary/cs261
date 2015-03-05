package team16.cs261.common.meta;

import java.util.EnumSet;

/**
 * Created by martin on 03/03/15.
 */
public enum FactorGroup {
    TRADER(FactorArity.NODE, "Trader -> Trader"),
    SYMBOL(FactorArity.NODE, "Symbol -> Symbol"),
    SECTOR(FactorArity.NODE, "Sector -> Sector"),
    TRADER_TRADER(FactorArity.EDGE, "Trader A -> Trader B"),
    TRADER_SYMBOL(FactorArity.EDGE, "Trader -> Symbol"),
    TRADER_SECTOR(FactorArity.EDGE, "Trader -> Sector");

    FactorArity arity;
    String label;

    FactorGroup(FactorArity arity, String label) {
        this.arity = arity;
        this.label = label;
    }

    public FactorArity getArity() {
        return arity;
    }

    public String getLabel() {
        return label;
    }

    public static EnumSet<FactorGroup> getGroups(FactorArity arity) {
        EnumSet<FactorGroup> set = EnumSet.noneOf(FactorGroup.class);

        for (FactorGroup group : FactorGroup.values()) {
            if (group.getArity() == arity) {
                set.add(group);
            }
        }

        return set;
    }

}
