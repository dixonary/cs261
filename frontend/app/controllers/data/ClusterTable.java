package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.ComparableExpressionBase;
import models.meta.ClusterStatus;
import org.springframework.stereotype.Controller;
import team16.cs261.common.querydsl.entity.Cluster;
import team16.cs261.common.querydsl.entity.QCluster;
import team16.cs261.common.querydsl.entity.QTick;
import util.datatables.Column;
import util.datatables.DataTable;
import util.datatables.Selection;
import util.datatables.filters.ColumnFilter;
import util.datatables.filters.StringFilter;
import util.datatables.filters.TimeFilter;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by martin on 05/03/15.
 */

@Controller
public class ClusterTable extends DataTable<Cluster> {

    private final QCluster c = new QCluster("c");
    private final QTick t = new QTick("t");
    private final ComparableExpressionBase[] columns = {
            c.id, c.status, c.time, c.nodes
    };

    @Override
    public String getSource() {
        //return controllers.data.routes.ClusterTable.query().toString();
        return routes.ClusterTable.query().toString();
    }


    @Override
    public Column[] getColumnDefs() {

        List<Selection> s = new ArrayList<>();
        for (ClusterStatus i : ClusterStatus.values()) {
            s.add(new Selection(i.name(), i.label));
        }

        return new Column[] {
                new Column("id"),
                new Column("status", new StringFilter(c.status, s, true)),
                new Column("time", new TimeFilter(t.start, 1420070400000L,1427842799999L)),
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
        BooleanBuilder bb = new BooleanBuilder();

        Column[] cds = getColumnDefs();

        for (int i = 0; i < cds.length; i++) {
            Column cd = cds[i];
            if (cd.getFilter() == null) continue;
            //if(i!=2)continue;
            ColumnFilter cf = cd.getFilter();

            String queryParam = "columns[" + i + "][search][value]";
            String queryString = request().getQueryString(queryParam);

            System.out.println("qs["+i+"] = " + queryString);

            Predicate pred = cf.getPredicate(queryString);

            bb.and(pred);
        }

        return bb;
    }

    @Override
    public Expression<Cluster> getProjection() {
        return Projections.bean(
                Cluster.class, c.id, c.tick, c.time, c.nodes, c.status
        );
    }


}
