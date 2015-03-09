package controllers.data;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import models.FactorClassDto;
import models.FactorDto;
import models.graph.EdgeDto;
import models.graph.NodeDto;
import models.meta.ClusterStatus;
import org.springframework.stereotype.Controller;
import team16.cs261.common.meta.FactorClass;
import team16.cs261.common.querydsl.entity.*;
import util.datatables.*;
import util.FactorClassUtil;
import util.Filters;
import util.datatables.filters.IdFilter;
import util.datatables.filters.StringFilter;
import util.datatables.filters.TimeFilter;

import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

/**
 * Created by martin on 05/03/15.
 */

@Controller
public class FactorTable extends DataTable<FactorDto> {

    QFactor f = QFactor.Factor;
    QTick t = QTick.Tick;
    QEdge e = QEdge.Edge;
    QNode n1 = new QNode("n1");
    QNode n2 = new QNode("n2");

    QCluster c = QCluster.Cluster;
    QClusterNode cn = QClusterNode.ClusterNode;

    private final ComparableExpressionBase[] columns = {
            f.id, t.start, f.factor, null, f.value, f.centile, f.sig
    };

    @Override
    public String getSource() {
        return controllers.data.routes.FactorTable.query().toString();
    }




    @Override
    public Column[] getColumnDefs() {

        //List<DomainValue> values = new ArrayList<>();

        //Map<String, String> factors = new HashMap<>();

        List<Selection> factorSelect = new ArrayList<>();
        for (FactorClassUtil.Item i : FactorClassUtil.getFactorTree()) {
            factorSelect.add(new Selection(i.value, i.label));
        }

        List<Selection> edges = new ArrayList<>();



        //for(Edge e : )
        /*for (FactorClassUtil.Item i : FactorClassUtil.getFactorTree()) {
            factors.add(new DomainValue<>(i.value, i.label));
        }*/

        return new Column[]{
                new Column("id"),
                new Column("time", new TimeFilter(t.start, 1420070400000L,1427842799999L)),
                new Column("factor", new StringFilter(f.factor, factorSelect, false)),
                new Column("edge", new IdFilter(f.edge, edges, true)),
                new Column("value"),
                new Column("centile"),
                new Column("sig"),
        };
    }


    @Override
    public SQLQuery getQuery() {
        //return template.newSqlQuery().from(f).join(t).on(f.tick.eq(t.tick));


        //SQLQuery nodes = template.newSqlQuery().from(cn).where(cn.cluster.eq(clId)).list;


        return template.newSqlQuery()
                .from(f).where(getViewPredicate())
                .join(t).on(f.tick.eq(t.tick))
                .join(e).on(f.edge.eq(e.id))
                .join(n1).on(e.source.eq(n1.id))
                .join(n2).on(e.target.eq(n2.id));
                //.where(getViewPredicate());
    }

    @Override
    public OrderSpecifier getOrder() {
        int orderColumn = Integer.parseInt(request().getQueryString("order[0][column]"));
        boolean asc = "asc".equals(request().getQueryString("order[0][dir]"));

        return asc ? columns[orderColumn].asc() : columns[orderColumn].desc();
    }


    /**
     * pre-filters datatable rows based on context.
     *
     * @return
     */
    @Override
    public Predicate getViewPredicate() {
        try {
            int clusterId = Integer.parseInt(request().getQueryString("clusterId"));

            NumberSubQuery<Integer> tick = new SQLSubQuery().from(c).where(c.id.eq(clusterId)).unique(c.tick);
            ListSubQuery<Integer> clusterNodes = new SQLSubQuery().from(cn).where(cn.cluster.eq(clusterId)).list(cn.node);
            ListSubQuery<Integer> clusterEdges = new SQLSubQuery().from(e).where(e.source.in(clusterNodes).and(e.target.in(clusterNodes))).list(e.id);

            return f.tick.eq(tick).and(f.edge.in(clusterEdges));
            //return  e.source.in(clusterNodes).and(e.target.in(clusterNodes));
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public Predicate getPredicate() {
        BooleanExpression fbt = Filters.timerangeFilter(t.start, request().getQueryString("columns[1][search][value]"));
        BooleanExpression fcf = factorClassFilter(request().getQueryString("columns[2][search][value]"));

        return new BooleanBuilder().and(fbt).and(fcf).getValue();
    }

    @Override
    public ConstructorExpression<FactorDto> getProjection() {
/*        return Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start, f.factor, e.id, f.value, f.centile, f.sig
        );*/
        return Projections.constructor(
                FactorDto.class, f.id, f.tick, t.start,
                Projections.constructor(
                        FactorClassDto.class, f.factor
                ),
                Projections.constructor(
                        EdgeDto.class, f.edge
                ),
                Projections.constructor(
                        NodeDto.class, n1.id, n1.label
                ),
                Projections.constructor(
                        NodeDto.class, n2.id, n2.label
                ),
                f.value, f.centile, f.sig
        );
    }


    public BooleanExpression factorClassFilter(String filterString) {
        EnumSet<FactorClass> classes = FactorClassUtil.getClasses(filterString);

        if (classes == null) return null;

        List<String> names = new ArrayList<>();
        for (FactorClass fc : classes) {
            names.add(fc.name());
        }

        return f.factor.in(names);
    }

}
