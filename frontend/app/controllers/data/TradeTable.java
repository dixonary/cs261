package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import models.TradeDto;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.QSector;
import team16.cs261.common.querydsl.entity.QSymbol;
import team16.cs261.common.querydsl.entity.QTrade;
import team16.cs261.common.querydsl.entity.QTrader;
import util.datatables.Column;
import util.datatables.DataTable;
import util.datatables.Selection;
import util.datatables.filters.ColumnFilter;
import util.datatables.filters.StringFilter;
import util.datatables.filters.TimeFilter;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static play.mvc.Controller.request;

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

        List<Selection> traders = template.query(
                template.newSqlQuery().from(trdr),
                Projections.constructor(Selection.class, trdr.id.stringValue(), trdr.email));
        List<Selection> symbols = template.query(
                template.newSqlQuery().from(sy),
                Projections.constructor(Selection.class, sy.id.stringValue(), sy.symbol));
        List<Selection> sectors = template.query(
                template.newSqlQuery().from(se),
                Projections.constructor(Selection.class, se.id.stringValue(), se.sector));

        return new Column[]{
                new Column("id"),
                new Column("time", new TimeFilter(t.time, 1420070400000L, 1427842799999L)),
                new Column("buyer", new StringFilter(t.buyerId.stringValue(), traders, true)),
                new Column("seller", new StringFilter(t.sellerId.stringValue(), traders, true)),
                new Column("price"),
                new Column("size"),
                new Column("currency"),
                new Column("symbol", new StringFilter(t.symbolId.stringValue(), symbols, true)),
                new Column("sector", new StringFilter(t.sectorId.stringValue(), sectors, true)),
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
        BooleanBuilder bb = new BooleanBuilder();

        Column[] cds = getColumnDefs();

        for (int i = 0; i < cds.length; i++) {
            Column cd = cds[i];
            if (cd.getFilter() == null) continue;
            ColumnFilter cf = cd.getFilter();

            String queryParam = "columns[" + i + "][search][value]";
            String queryString = request().getQueryString(queryParam);

            Predicate pred = cf.getPredicate(queryString);

            bb.and(pred);
        }

        return bb;
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
