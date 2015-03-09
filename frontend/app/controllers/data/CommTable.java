package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.StringExpression;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.*;
import util.datatables.*;
import util.datatables.filters.ColumnFilter;
import util.datatables.filters.IdFilter;
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
public class CommTable extends DataTable<Comm> {

    QComm c = QComm.Comm;

    private final ComparableExpressionBase[] columns = {
            c.id, c.time, c.sender, c.recipient
    };

    @PostConstruct
    public void init() {


    }

    public String getSource() {
        return routes.CommTable.query().toString();
    }

    @Override
    public Column[] getColumnDefs() {

        QTrader trdr = QTrader.Trader;

        List<Selection> traders = template.query(
                template.newSqlQuery().from(trdr),
                Projections.constructor(Selection.class, trdr.id, trdr.email));


        return new Column[]{
                new Column("id"),
                new Column("time", new TimeFilter(c.time, 1420070400000L,1427842799999L)),
                new Column("sender", new StringFilter(c.senderId.stringValue(), traders, true)),
                new Column("recipient", new StringFilter(c.recipientId.stringValue(), traders, true)),
        };
    }

    @Override
    public SQLQuery getQuery() {
        return template.newSqlQuery()
                .from(c);
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
    public Expression<Comm> getProjection() {
        return Projections.bean(
                Comm.class,
                c.id, c.time, c.tick,
                c.senderId, c.sender,
                c.recipientId, c.recipient
        );
    }

    @Override
    public String getCsvHeader() {
        return "time,buyer,seller,price,size,currency,symbol,sector,bid,ask";
    }

    @Override
    public List<StringExpression> getCsvColumns() {
        return Arrays.asList(
                c.time.stringValue(),
                c.sender, c.recipient
        );
    }

    private Expression<String> concatWs(String s, Object... o) {
        return Expressions.stringTemplate("CONCAT_WS( 'asd', [{0}] )", o);
    }


}
