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
        final String p = "_traders";

        switch (fc) {
            case COMMON:

                return routes.Application.tradesByTraders(p, p);
                //c = routes.Application.tradesQuery(p, p, "", "");
                //c = routes.Application.tradesQuery()
                //c = routes.ref.Application.tradesQuery(p, p, null, null);
            case COMMON_BUYS:
                return routes.Application.tradesByBuyer(p);
                //c = routes.Application.tradesQuery(p, "", "", "");
            case COMMON_SELLS:
                return routes.Application.tradesBySeller(p);
                //c = routes.Application.tradesQuery("", p, "", "");
            case COMMS:
                return routes.Application.commsBy(p, p);
                //c = routes.Application.tradesQuery("", "%TRADERS%", "", "");
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
