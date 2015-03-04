package team16.cs261.backend.module.impl;

import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.clustering.mcl.SparseMatrix;
import net.sf.javaml.core.Dataset;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.Module;
import team16.cs261.backend.util.*;
import team16.cs261.backend.util.Timer;
import team16.cs261.common.dao.*;
import team16.cs261.common.entity.Tick;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.graph.Edge;

import java.io.IOException;
import java.util.*;

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
    GraphDao graphDao;

    @Autowired
    private Config config;

    @Autowired
    public AnalyserModule(Config config) throws IOException {
        super(config, "ANALYSER");
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }

    int trades = 0;
    long timeTaken = 0;


    public static final String CALL_AGGR = "CALL Aggr(?)";
    //public static final String UPDATE_COUNTS = "CALL UpdateCounts(?, ?)";
    //public static final String CALL_OUTPUT = "CALL Output(?, ?, ?, ?, ?)";

    Timer timer;

    @Transactional
    public void process() {


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


        Double lambda = jdbcTemplate.queryForObject(
                "SELECT commsMa / (SELECT count(*) FROM TraderPair) FROM Tick WHERE tick = ?",
                new Object[]{tick}, Double.class);
        Integer maxComms = jdbcTemplate.queryForObject("SELECT max(comms) FROM TraderPair", Integer.class);
        //System.out.println("Comms: " + lambda);


        //SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT trader1, trader2, common")


        //outputs
        //final Timer outputTime = new Timer(CALL_OUTPUT);

        int window = 8;

        Tick avgTick = tickDao.selectAvgWhereId(tick, window);

        System.out.println("Averaged: " + avgTick);

        /*Double meanCommSym = jdbcTemplate.queryForObject(
                "SELECT avg(meanCommSym) FROM Tick WHERE tick <= ?",
                *//*"SELECT avg(meanCommSym) FROM Tick WHERE tick = ?",*//*
                new Object[]{tick}, Double.class
        );*/

        double sig = 0.05D;

        updateFactors(tick, "COMMON", "TraderPair", "common", avgTick.getCommonPerPair(), sig);
        updateFactors(tick, "COMMON_BUYS", "TraderPair", "commonBuys", avgTick.getCommonBuysPerPair(), sig);
        updateFactors(tick, "COMMON_SELLS", "TraderPair", "commonSells", avgTick.getCommonSellsPerPair(), sig);
        updateFactors(tick, "COMMS", "TraderPair", "comms", avgTick.getCommsPerPair(), sig);



        //outputTime.stop();


        //timer = new Timer(UPDATE_WEIGHTS);
        //jdbcTemplate.update(UPDATE_WEIGHTS, tick);
        //System.out.println(timer);

        findClusters(tick);



   /*     */

        //mcl.run(sm, 0.001D, 2D, 0D, 0.001D);
        //System.out.println("Mcl: "+ sm);

        //long clusteringTime = System.currentTimeMillis() - a;
        //System.out.println("Clustering time: " + clusteringTime);

        //jdbcTemplate.update("UPDATE Tick SET analysed = TRUE, analysisTime = ? WHERE tick = ?", analysisTime, tick);
        jdbcTemplate.update(
                "UPDATE Tick SET status = 'ANALYSED', aggrTime = ?, analysisTime = ?, commsGraph = ? WHERE tick = ?",
                aggrTime.getElapsed(), analysisTime.getElapsed(), "", tick);



/*
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

    }


    public static final String UPDATE_FACTORS =
            "INSERT INTO Factor (tick, edge, factor, value, centile, sig) " +
                    "SELECT ?, id, ?, #fld, (select cdf from Poisson where x = #fld), (select sig from Poisson where x = #fld) " +
                    "FROM #tbl " +
                    "WHERE #fld >= ?";

    public static final String UPDATE_FACTOR_FREQS =
            "INSERT INTO FactorFreq (tick, factor, x, fx) " +
                    "SELECT ?, ?, #fld, count(*) " +
                    "FROM #tbl group by #fld;";

    public void updateFactors(long tick, String f, String table, String field, double lambda, double sig) {
        PoissonDistribution dist = new PoissonDistribution(lambda);
        int threshold = dist.inverseCumulativeProbability(1 - sig) + 1;


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
        String sql = UPDATE_FACTORS.replaceAll("#fld", field).replace("#tbl", table);
        System.out.println("sql: " + sql);
        jdbcTemplate.update(sql, tick, f, threshold);


        String updateFactorFreqs = UPDATE_FACTOR_FREQS.replaceAll("#fld", field).replace("#tbl", table);
        System.out.println("sql: " + updateFactorFreqs);
        jdbcTemplate.update(updateFactorFreqs, tick, f);


        // save frequency table


        // update centiles


    }


    public static final String SELECT_FACTOR_EDGES =
            "SELECT E.id AS id, E.source AS source, E.target AS target, sum(centile) AS weight FROM Edge E JOIN Factor F ON E.id = F.edge WHERE tick = ? GROUP BY edge";

    // Maximum difference between row elements and row square sum (measure of
    // idempotence)
    private double maxResidual = 0.001;

    // inflation exponent for Gamma operator
    private double pGamma = 2.0;

    // loopGain values for cycles
    private double loopGain = 1.0;

    // maximum value considered zero for pruning operations
    private double maxZero = 0.001;

    public void findClusters(long tick) {
        Integer maxIndex = jdbcTemplate.queryForObject("SELECT max(id) FROM Node", Integer.class);
        List<Edge> edges = jdbcTemplate.query(SELECT_FACTOR_EDGES, new Object[]{tick}, new BeanPropertyRowMapper<>(Edge.class));

        double[][] weights = new double[maxIndex + 1][maxIndex + 1];

        for (Edge e : edges) {

            System.out.println("Edge: " + e);

            int s = e.getSource();
            int t = e.getTarget();
            double w = e.getWeight();

            weights[s][t] = w;
            weights[t][s] = w;
        }

        SparseMatrix sm = new SparseMatrix(weights);

        System.out.println("Size: " + Arrays.toString(sm.getSize()));

        //MarkovClustering mcl = new MarkovClustering();
        //SparseMatrix sm2 = mcl.run(sm, maxResidual, pGamma, 0., maxZero);

        Clusters cl = new Clusters();
        SparseMatrix sm2 = cl.cluster2(sm);

        //System.out.println("SM1: \n" + sm.toStringDense());
        //System.out.println("SM2: \n" + sm2.toStringDense());


        String insertEdges = "INSERT INTO ClusterEdge (tick, edge, weight) VALUES (?, ?, ?)";
        List<Object[]> args = new ArrayList<>();

        System.out.println("SM2: " + Arrays.toString(sm2.getSize()));
        sm2.adjustMaxIndex(maxIndex, maxIndex);
        double[][] clusters = sm2.getDense();
        System.out.println("clusters[][]: " + clusters.length + ", " + clusters[0].length);

        for (Edge e : edges) {
            int src = e.getSource();
            int trg = e.getTarget();

            double weight;
            weight = src > trg ? sm2.get(src, trg) : sm2.get(trg, src);

            if(weight > 0.98) {
            //if (sm2.get(src, trg) > 0 || sm2.get(trg, src) > 0) {
                //System.out.println("s: " + s + ", t: " + t);

                //System.out.println(String.format("%s : %s, %s", weight, sm2.get(s,t), sm2.get(t,s)));

                args.add(new Object[]{tick, e.getId(), weight});
            }

            /*            //double newWeightA = clusters[e.getSource()][e.getTarget()];
            //double newWeightB = clusters[e.getTarget()][e.getSource()];

            //System.out.println(newWeightA + " : " + newWeightB);

            if(newWeightA > 0) {
                System.out.println("Edge: " + e);

                args.add(new Object[]{tick, e.getId(), newWeightA});
            }*/


        }
        jdbcTemplate.batchUpdate(insertEdges, args);


        //System.out.println("nodes: " + nodeSet);
        //System.out.println("nodes: " + ds);

//System.out.println("before: " + sm);

        /*AbstractSimilarity am = new AbstractSimilarity() {
            @Override
            public double measure(Instance x, Instance y) {
                return sm.get(x,y);
            }
        };*/






        /*System.out.println("after: ");
        for (Dataset ds : clusters) {
            System.out.println(ds);
            for (int i = 0; i < ds.size(); i++) {
                //ds.get(i).getID()
            }
        }*/


  /*      Map<Integer, Set<Integer>> l = graphDao.getComms().map;


        ObjectMapper mapper = new ObjectMapper();
        String commsGraph = null;
        try {
            commsGraph = mapper.writeValueAsString(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

/*        Clusters cl = new Clusters();

        Dataset[] clusters = cl.cluster(sm);

        System.out.println("after: ");
        for (Dataset ds : clusters) {
            System.out.println(ds);
            for (int i = 0; i < ds.size(); i++) {
                //ds.get(i).getID()
            }
        }*/


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
