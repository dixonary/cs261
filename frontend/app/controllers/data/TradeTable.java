package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import models.*;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.*;
import util.datatables.*;
import util.datatables.filters.ColumnFilter;
import util.datatables.filters.IdFilter;
import util.datatables.filters.TimeFilter;

import javax.annotation.PostConstruct;
import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

/**
 * Created by martin on 05/03/15.
 */

@Controller
public class TradeTable extends DataTable<TradeDto> {

    QTrade t = QTrade.Trade;

    private final ComparableExpressionBase[] columns = {
            t.id, t.time, t.buyer, t.seller,
            t.price, t.size, t.currency, t.symbol, t.sector,
            t.bid, t.ask
    };

    @PostConstruct
    public void init() {


    }

    public String getSource() {
        return controllers.data.routes.TradeTable.query().toString();
    }

    @Override
    public Column[] getColumnDefs() {

        QTrader trdr = QTrader.Trader;
        QSymbol sy = QSymbol.Symbol;
        QSector se = QSector.Sector;

        List<DomainValue> traders = template.query(
                template.newSqlQuery().from(trdr),
                Projections.constructor(DomainValue.class, trdr.id, trdr.email));
        List<DomainValue> symbols = template.query(
                template.newSqlQuery().from(sy),
                Projections.constructor(DomainValue.class, sy.id, sy.symbol));
        List<DomainValue> sectors = template.query(
                template.newSqlQuery().from(se),
                Projections.constructor(DomainValue.class, se.id, se.sector));


        return new Column[]{
                new Column("id"),
                new Column("time", new TimeFilter(t.time, 1420070400000L,1427842799999L)),
                new Column("buyer",new IdFilter( t.buyerId, traders, true)),
                new Column("seller", new IdFilter(t.sellerId, traders, true)),
                new Column("price"),
                new Column("size"),
                new Column("currency"),
                new Column("symbol", new IdFilter(t.symbolId, symbols, true)),
                new Column("sector", new IdFilter( t.sectorId, sectors, true)),
        };
    }

    @Override
    public SQLQuery getQuery() {
        return template.newSqlQuery()
                .from(t);
    }

    @Override
    public OrderSpecifier getOrder() {
        int orderColumn = Integer.parseInt(request().getQueryString("order[0][column]"));
        boolean asc = "asc".equals(request().getQueryString("order[0][dir]"));

        return asc ? columns[orderColumn].asc() : columns[orderColumn].desc();
    }


    @Override
    public Predicate getPredicate() {
        //BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));
        //BooleanExpression fcf = factorClassFilter(request().getQueryString("columns[2][search][value]"));

        BooleanBuilder bb = new BooleanBuilder();

        Column[] cds = getColumnDefs();

        for (int i = 0; i < cds.length; i++) {
            Column cd = cds[i];
            if(cd.getFilter() == null) continue;
            ColumnFilter cf= cd.getFilter();

            String queryParam = "columns[" + i + "][search][value]";
            String queryString = request().getQueryString(queryParam);

            Predicate pred = cf.getPredicate(queryString);

            //System.out.println(cd.getName() + " filter: " + queryString);

            bb.and(pred);
        }

        //String buyerString = request().getQueryString("buyer");
        //System.out.println("buyers: " + buyerString);

        return bb;
        //return new BooleanBuilder().and(fbt).and(fcf).getValue();
    }

    @Override
    public ConstructorExpression<TradeDto> getProjection() {
/*        return Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start, f.factor, e.id, f.value, f.centile, f.sig
        );*/


        return Projections.constructor(
                TradeDto.class,
                t.id, t.time, t.tick,
                t.buyerId, t.buyer, t.sellerId, t.seller,
                t.price, t.size, t.currency,
                t.symbolId, t.symbol, t.sectorId, t.sector,
                t.bid, t.ask
        );
    }

    @Override
    public String getCsvHeader() {
        return "time,buyer,seller,price,size,currency,symbol,sector,bid,ask";
    }

    @Override
    public List<StringExpression> getCsvColumns() {
        return Arrays.asList(
                t.time.stringValue(), t.buyer, t.seller,
                t.price.stringValue(), t.size.stringValue(),
                t.currency, t.symbol, t.sector,
                t.bid.stringValue(), t.ask.stringValue()
        );
    }

    private Expression<String> concatWs(String s, Object... o) {
        return Expressions.stringTemplate("CONCAT_WS( 'asd', [{0}] )", o);
    }


}
