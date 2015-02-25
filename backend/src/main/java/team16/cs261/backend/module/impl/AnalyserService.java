package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.AnalyserModule;
import team16.cs261.dal.dao.*;
import team16.cs261.dal.entity.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Component
public class AnalyserService extends AnalyserModule {

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
    TraderStockDao traderStockDao;

    @Autowired
    private Config config;

    @Autowired
    public AnalyserService(Config config) throws IOException {
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


    public static final String UPDATE_SYMBOLS = "CALL UpdateSymbols(?,?,?)";
    public static final String UPDATE_TRADER_PAIRS = "CALL UpdateTraderPairs(?,?,?)";

    @Transactional
    public void process() {
//System.out.println("process()");

        Long time = jdbcTemplate.queryForObject("SELECT max(time) FROM TimeInterval WHERE analysed = FALSE", Long.class);
        if (time == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        long fromA = time - config.getTimeShort();
        long fromB = time - config.getTimeLong();


        jdbcTemplate.update(UPDATE_SYMBOLS, fromA, fromB, time);
        jdbcTemplate.update(UPDATE_TRADER_PAIRS, fromA, fromB, time);

        //jdbcTemplate.update(UPDATE_TRADER_PAIR_COMM_RATES,
        //      fromA, to, fromB, to);


        jdbcTemplate.update("UPDATE TimeInterval SET analysed = TRUE WHERE time = ?", time);
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

        float decayFactor = 0.9; //Estimation, time decay per hour

        //1. Get the current affinity from the TraderStock table (if none then 0)
        //2. Get the time difference between last update and now
        //3. Divide by decayFactor to the power of time difference (in hours)
        //4. Store new affinity in the TraderStock table
    }

}
