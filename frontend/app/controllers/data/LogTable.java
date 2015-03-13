package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.*;
import util.datatables.Column;
import util.datatables.DataTable;
import util.datatables.Selection;
import util.datatables.filters.ColumnFilter;
import util.datatables.filters.TimeFilter;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by martin on 05/03/15.
 */

@Controller
public class LogTable extends DataTable<Log> {

    QLog l = QLog.Log;

    private final ComparableExpressionBase[] columns = {
            l.id, l.time, l.type, l.message
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
                Projections.constructor(Selection.class, trdr.id, trdr.email));
        List<Selection> symbols = template.query(
                template.newSqlQuery().from(sy),
                Projections.constructor(Selection.class, sy.id, sy.symbol));
        List<Selection> sectors = template.query(
                template.newSqlQuery().from(se),
                Projections.constructor(Selection.class, se.id, se.sector));

        return new Column[]{
                new Column("id"),
                new Column("time", new TimeFilter(l.time, 1420070400000L, 1427842799999L)),
                new Column("type"),
                new Column("message")
        };
    }

    @Override
    public SQLQuery getQuery() {
        return template.newSqlQuery()
                .from(l);
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
    public Expression<Log> getProjection() {
        return Projections.bean(
                Log.class,
                l.id, l.time, l.type, l.message
        );
    }

    @Override
    public String getCsvHeader() {
        return "time,buyer,seller,price,size,currency,symbol,sector,bid,ask";
    }

    @Override
    public List<StringExpression> getCsvColumns() {
        return Arrays.asList(
                l.id.stringValue(), l.time.stringValue(), l.type, l.message
        );
    }

}
