package team16.cs261.common.meta;

/**
* Created by martin on 05/03/15.
*/
public enum FactorArity   {
    NODE("Self-reference"),
    EDGE("Distinct link");

    String label;

    FactorArity(String label) {
        this.label = label;
    }


    public String getLabel() {
        return label;
    }
}
