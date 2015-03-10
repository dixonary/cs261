package models;

import controllers.routes;
import play.api.mvc.Call;
import team16.cs261.common.meta.FactorClass;

/**
 * Created by martin on 08/03/15.
 */
public class FactorClassDto {


    private FactorClass factor;

    private String query;
    private String label;

    public FactorClassDto(String factorName) {

        this.factor = FactorClass.valueOf(factorName);




        Call c = getCall(factor);


        if (c != null)
            this.query = c.toString();

        this.label = factor.getLabel();
    }

    private static Call getCall(FactorClass fc) {
        final String s = "_traders";

        switch (fc) {
            case COMMON:
                return routes.Application.tradesBy(null, s, s, null, null);
            case COMMON_BUYS:
                return routes.Application.tradesBy(null, s, null, null, null);
            case COMMON_SELLS:
                return routes.Application.tradesBy(null, null, s, null, null);
            case COMMS:
                return routes.Application.commsBy(null, s, s);
        }

        return null;
    }

    public FactorClass getFactor() {
        return factor;
    }

    public void setFactor(FactorClass factor) {
        this.factor = factor;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
