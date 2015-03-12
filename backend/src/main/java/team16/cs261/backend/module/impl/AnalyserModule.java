package team16.cs261.backend.module.impl;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.model.Graph;
import team16.cs261.backend.model.MclOutput;
import team16.cs261.backend.module.Module;
import team16.cs261.backend.service.MclService;
import team16.cs261.backend.service.impl.MclServiceImpl;
import team16.cs261.backend.util.Timer;
import team16.cs261.common.dao.*;
import team16.cs261.common.entity.Tick;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.graph.Edge;
import team16.cs261.common.meta.FactorClass;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Component
public class AnalyserModule extends Module {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommDao commDao;
    @Autowired
    TraderDao traderDao;
    @Autowired
    TradeDao tradeDao;
    @Autowired
    SymbolDao symbolDao;
    @Autowired
    SectorDao sectorDao;

    @Autowired
    TickDao tickDao;

    @Autowired
    FactorDao factorDao;

    @Autowired
    TraderStockDao traderStockDao;

    @Autowired
    LogDao logDao;

    @Autowired
    private Config config;

    @Autowired
    public AnalyserModule(Config config) throws IOException {
        super(config, "ANALYSER");
    }


    Map<Long, Future<MclOutput>> mclOutputs = new HashMap<>();

    @PostConstruct
    public void init() {

        final String deleteFC = "DELETE FROM FactorClass";
        final String insertFC = "INSERT INTO FactorClass (factor, label, sig, weight) VALUES (?, ?, ?, ?)";

        List<Object[]> args = new ArrayList<>();
        for (FactorClass fc : FactorClass.getImplemented()) {
            args.add(new Object[]{fc.name(), fc.getLabel(), fc.sig, fc.weight});
        }

        jdbcTemplate.update(deleteFC);
        jdbcTemplate.batchUpdate(insertFC, args);


    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }



    public static final String CALL_AGGR = "CALL Aggr(?)";
    Timer timer;

    @Transactional
    public void process() {


        for (Map.Entry<Long, Future<MclOutput>> entry : mclOutputs.entrySet()) {
            Future<MclOutput> future = entry.getValue();
            if (!future.isDone()) continue;

            long tick = entry.getKey();


            try {
                MclOutput out = future.get();
                if(out==null) {
                    logDao.log("CLUSTERING", "Failed to find clusters for tick " + tick);
                    return;
                }

                List<Set<Integer>> clusters = future.get().getClusters();

                jdbcTemplate.update(
                        "UPDATE Tick SET status = ?, clusterCount = ?, clusters = ? WHERE tick = ?",
                        "CLUSTERED", clusters.size(), toJson(clusters), tick);

                insertClusters(tick, clusters);

                mclOutputs.remove(tick);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //if(true)return;

        Long tick = jdbcTemplate.queryForObject("SELECT min(tick) FROM Tick WHERE status = 'PARSED'", Long.class);
        //Tick tick = jdbcTemplate.queryForObject("SELECT min(tick) FROM Tick WHERE status = 'PARSED'", Long.class);
        if (tick == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("process()");
        final Timer analysisTime = new Timer("Analysis time");


        final Timer aggrTime = new Timer(CALL_AGGR);
        jdbcTemplate.update(CALL_AGGR, tick);
        aggrTime.stop();
        //System.out.println(timer);


        Object[] args = new Object[]{tick, config.analysis.comms.intervals};

        /*final Timer updateCountsTime = new Timer(UPDATE_COUNTS);
        jdbcTemplate.update(UPDATE_COUNTS, args);
        updateCountsTime.stop();*/


        int window = 24;

        Tick avgTick = tickDao.selectAvgWhereId(tick, window);

        System.out.println("Averaged: " + avgTick);

        /*Double meanCommSym = jdbcTemplate.queryForObject(
                "SELECT avg(meanCommSym) FROM Tick WHERE tick <= ?",
                *//*"SELECT avg(meanCommSym) FROM Tick WHERE tick = ?",*//*
                new Object[]{tick}, Double.class
        );*/

        double sig = 0.05D;

        //updateFactors(tick, "COMMON", "TraderPair", "common", avgTick.getCommonPerPair(), FactorClass.COMMON.sig);
        updateFactors(tick, "COMMON_BUYS", "TraderPair", "commonBuys", avgTick.getCommonBuysPerPair(), FactorClass.COMMON_BUYS.sig);
        updateFactors(tick, "COMMON_SELLS", "TraderPair", "commonSells", avgTick.getCommonSellsPerPair(), FactorClass.COMMON_SELLS.sig);
        updateFactors(tick, "COMMS", "TraderPair", "comms", avgTick.getCommsPerPair(), FactorClass.COMMS.sig);


        //outputTime.stop();


        //timer = new Timer(UPDATE_WEIGHTS);
        //jdbcTemplate.update(UPDATE_WEIGHTS, tick);
        //System.out.println(timer);

        findClusters(tick);

        //mcl.run(sm, 0.001D, 2D, 0D, 0.001D);
        //System.out.println("Mcl: "+ sm);

        //long clusteringTime = System.currentTimeMillis() - a;
        //System.out.println("Clustering time: " + clusteringTime);

        //jdbcTemplate.update("UPDATE Tick SET analysed = TRUE, analysisTime = ? WHERE tick = ?", analysisTime, tick);
        jdbcTemplate.update(
                "UPDATE Tick SET status = 'ANALYSED', aggrTime = ?, analysisTime = ? WHERE tick = ?",
                aggrTime.getElapsed(), analysisTime.getElapsed(), tick);



    }


/*    public static final String UPDATE_FACTORS =
            "INSERT INTO Factor (tick, edge, factor, value, centile, sig) " +
                    "SELECT ?, id, ?, #fld, (select cdf from Poisson where x = #fld), (select sig from Poisson where x = #fld) " +
                    "FROM #tbl " +
                    "WHERE #fld >= ?";*/

    public static final String UPDATE_FACTORS =
            "INSERT INTO Factor (tick, edge, factor, value, centile, sig, score, weight) " +
                    "SELECT ?, id, ?, #fld, cdf, P.sig, 1-(P.sig/FC.sig), FC.weight " +
                    "FROM #tbl " +
                    "JOIN FactorClass FC ON FC.factor = ? " +
                    "JOIN Poisson P ON P.x = #fld " +
                    //"WHERE P.sig < ?";
                    " WHERE #fld > ? ";

    public static final String UPDATE_FACTOR_FREQS =
            "INSERT INTO FactorFreq (tick, factor, x, fx) " +
                    "SELECT ?, ?, #fld, count(*) " +
                    "FROM #tbl group by #fld;";

    public void updateFactors(long tick, String f, String table, String field, double lambda, double sigTh) {
        if (lambda == 0) return;

        PoissonDistribution dist = new PoissonDistribution(lambda);
        int threshold = dist.inverseCumulativeProbability(1 - sigTh) + 1;


        // insert poisson numbers
        String maxQtyQuery = "SELECT max(" + field + ") FROM " + table;
        Integer maxComms = jdbcTemplate.queryForObject(maxQtyQuery, Integer.class);

        jdbcTemplate.execute("DELETE FROM Poisson;");
        List<Object[]> poissonArgs = new ArrayList<>();

        for (int x = 0; x <= maxComms; x++) {
            //poissonArgs.add(new Object[]{lambda, x, p.pdf[x], p.cdf[x]});
            double sigLevel = 1;
            if (x > 0) {
                sigLevel = 1 - dist.cumulativeProbability(x - 1);
            }

            poissonArgs.add(new Object[]{lambda, x, dist.probability(x), dist.cumulativeProbability(x), sigLevel});
        }
        jdbcTemplate.batchUpdate("INSERT INTO Poisson VALUES (?, ?, ?, ?, ?)", poissonArgs);


        //
        String sql = UPDATE_FACTORS.replaceAll("#fld", field).replaceAll("#tbl", table);
        System.out.println("sql: " + sql);
        jdbcTemplate.update(sql, tick, f, f, threshold);


        String updateFactorFreqs = UPDATE_FACTOR_FREQS.replaceAll("#fld", field).replaceAll("#tbl", table);
        System.out.println("sql: " + updateFactorFreqs);
        jdbcTemplate.update(updateFactorFreqs, tick, f);


        // save frequency table


        // update centiles


    }


    public static final String SELECT_FACTOR_EDGES =
            "SELECT E.id AS id, E.source AS source, E.target AS target, sum(F.score * F.weight) AS weight FROM Edge E JOIN Factor F ON E.id = F.edge WHERE tick = ? GROUP BY edge";

    public static final String SELECT_FACTOR_EDGES2 =
            "SELECT E.id AS id, E.source, E.target, factor, score FROM Edge E JOIN Factor F ON E.id = F.edge WHERE tick = ? GROUP BY edge";

    @Autowired
    MclService mclService;

    public void findClusters(long tick) {
        List<Edge> edges = jdbcTemplate.query(SELECT_FACTOR_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(Edge.class));
        Graph g = new Graph(edges);

/*        List<Factor> factors = jdbcTemplate.query(SELECT_FACTOR_EDGES2, new Object[]{tick}, new BeanPropertyRowMapper<>(Factor.class));
        System.out.println("factors: " + factors);
        FactorGraph fg = new FactorGraph(factors);*/

        double[][] input4 = g.toMatrix();
        //double[][] output4 = mcl4.run(input4);
        //System.out.println("Cls: " + SqMatrix.prettyString(output4));
        //List<Set<Integer>> clusters4 = g.getClusterIds(mcl4.getClusters(output4));

        String mclLines = g.toMcl();
        mclOutputs.put(tick, mclService.run(tick, mclLines));


        final String updateTick = "UPDATE Tick SET mclInput = ?, clusters = ?, clusters2 = ?, clusters3 = ? WHERE tick = ?";
        //String tickMeta = toJson(mclOut.getClusters());
        //String tickMeta = toJson(mclOut.getClusters().values());
        String tickMeta = toJson(g.getNodeIds());
        //String meta3 = toJson(clusters3);
        //String meta4 = toJson(clusters4);
        jdbcTemplate.update(updateTick, g.toMcl(), tickMeta, null, null, tick);


        //insertClusters(tick, clusters4);



/*        System.out.println("legacy: \n" + legacyClusters.toStringDense());
        System.out.println("new: \n" + mclOut.getOutput().toStringDense());
        legacyClusters.hadamardProduct(mclOut.getOutput());
        System.out.println("diff: \n" + legacyClusters.toStringDense());*/


//        String insertEdges = "INSERT INTO TickClusterEdge (tick, edge, weight) VALUES (?, ?, ?)";
/*  //      List<Object[]> args = new ArrayList<>();

        //System.out.println("SM2: " + Arrays.toString(sm2.getSize()));
        //sm2.adjustMaxIndex(maxIndex, maxIndex);
        //double[][] clusters = sm2.getDense();
        //System.out.println("clusters[][]: " + clusters.length + ", " + clusters[0].length);

        for (Edge e : edges) {
            int src = e.getSource();
            int trg = e.getTarget();

            double weight;
            weight = src > trg ? legacyClusters.get(src, trg) : legacyClusters.get(trg, src);

            if (weight > 0.98) {
                //if (sm2.get(src, trg) > 0 || sm2.get(trg, src) > 0) {
                //System.out.println("s: " + s + ", t: " + t);

                //System.out.println(String.format("%s : %s, %s", weight, sm2.get(s,t), sm2.get(t,s)));

                args.add(new Object[]{tick, e.getId(), weight});
            }


        }
        jdbcTemplate.batchUpdate(insertEdges, args);*/


    }

    final String insertCluster =
            //"INSERT INTO Cluster (tick, nodes, edges, meta) VALUES (?, ?, ?, ?);";
            "INSERT INTO Cluster (tick, time, nodes, edges, meta) VALUES (?, ?, ?, ?, ?);";
    final String insertCN = "INSERT INTO ClusterNode (cluster, node) VALUES (?, ?)";

    public void insertClusters(long tick, List<Set<Integer>> clusters) {

        long now = System.currentTimeMillis();

        for (Set<Integer> cl : clusters) {
            String meta = toJson(cl);

            //Integer cluster = cl.iterator().next();
            jdbcTemplate.update(insertCluster, tick, now, cl.size(), -1, meta);
            Integer cluster = jdbcTemplate.queryForObject("SELECT max(id) FROM Cluster;", Integer.class);
            //Integer cluster = jdbcTemplate.queryForObject(insertCluster, new Object[]{tick, cl.size(), -1, meta}, Integer.class);
            //jdbcTemplate.update(insertCluster, tick, cluster, cl.size(), -1, meta);

            //System.out.println("cl: " + cluster);


            for (int clNode : cl) {
                //System.out.println("nd: " + clNode);
                jdbcTemplate.update(insertCN, cluster, clNode);
            }
        }

    }


    ObjectMapper om = new ObjectMapper();

    public String toJson(Object o) {
        try {
            return om.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }


    public static final String UPDATE_SYMBOL_TRADE_TOTAL = "CALL UpdateSymbolTradeTotal(?)";
    public static final String UPDATE_SYMBOL_TRADE_COUNTER1 = "CALL UpdateSymbolTradeRate1(?, ?, ?)";
    public static final String UPDATE_SYMBOL_TRADE_COUNTER2 = "CALL UpdateSymbolTradeRate2(?, ?, ?)";

    public void onTrade(List<Trade> tradeEnts) {

        List<Object[]> args1 = new ArrayList<>();
        List<Object[]> args2 = new ArrayList<>();
        List<Object[]> args3 = new ArrayList<>();

        long from2 = System.currentTimeMillis() - 1000 * 60;
        long from3 = System.currentTimeMillis() - 1000 * 60 * 5;
        long to = System.currentTimeMillis();

        for (Trade ent : tradeEnts) {
            args1.add(new Object[]{ent.getSymbol()});
            args2.add(new Object[]{ent.getSymbol(), from2, to});
            args3.add(new Object[]{ent.getSymbol(), from3, to});
        }
        // update symbol trade rates


        jdbcTemplate.batchUpdate(UPDATE_SYMBOL_TRADE_TOTAL, args1);
        jdbcTemplate.batchUpdate(UPDATE_SYMBOL_TRADE_COUNTER1, args2);
        jdbcTemplate.batchUpdate(UPDATE_SYMBOL_TRADE_COUNTER2, args3);


        traderStockDao.updateTraderStock(tradeEnts);

    }

    //Affinity is used to determine each trader's stock preferences
    //This function should be run on every new trade, for buyer AND seller
    public void updateAffinity(/*Arguments*/) {

        float decayFactor = 0.9f; //Estimation, time decay per hour

        //1. Get the current affinity from the TraderStock table (if none then 0)
        //2. Get the time difference between last update and now
        //3. Divide by decayFactor to the power of time difference (in hours)
        //4. Store new affinity in the TraderStock table
    }

}
