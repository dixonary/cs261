package controllers.data;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import models.ClusterDto;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.QCluster;
import team16.cs261.common.querydsl.entity.QTick;
import util.Filters;
import util.datatables.Column;
import util.datatables.DataTable;

import static play.mvc.Controller.request;

/**
 * Created by martin on 05/03/15.
 */

@Controller
public class ClusterTable extends DataTable<ClusterDto> {

    private final QCluster c = new QCluster("c");
    private final QTick t = new QTick("t");
    private final ComparableExpressionBase[] columns = {
            c.id, c.tick
    };

    @Override
    public String getSource() {
        return controllers.data.routes.ClusterTable.query().toString();
    }


    @Override
    public Column[] getColumnDefs() {
        return new Column[] {
                new Column("id"),
                new Column("time"),
                new Column("class"),
        };
    }

    @Override
    public SQLQuery getQuery() {
        return template.newSqlQuery().from(c).join(t).on(c.tick.eq(t.tick));
    }

    @Override
    public OrderSpecifier getOrder() {
        int orderColumn = Integer.parseInt(request().getQueryString("order[0][column]"));
        boolean asc = "asc".equals(request().getQueryString("order[0][dir]"));

        return asc ? columns[orderColumn].asc() : columns[orderColumn].desc();
    }

    @Override
    public Predicate getPredicate() {
        BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));

        return fbt;
        //return new BooleanBuilder().and(fbt).and(fcf).getValue();
    }

    @Override
    public ConstructorExpression<ClusterDto> getProjection() {
        return Projections.constructor(
                ClusterDto.class, c.id, c.tick, t.start, t.end
        );
    }


}
