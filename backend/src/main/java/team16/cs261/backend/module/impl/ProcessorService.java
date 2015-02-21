package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
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

@Service
public class ProcessorService extends AnalyserModule {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Autowired
    RawCommDao rawCommDao;
    @Autowired
    RawTradeDao rawTradeDao;

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
    public ProcessorService(Config config) throws IOException {
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

    //@Transactional
    public void process() {
        long started = System.nanoTime();

        List<RawTrade> rawTrades = rawTradeDao.selectAllLimit(100);
        List<RawComm> rawComms = rawCommDao.selectAllLimit(100);

        List<Integer> rawTradeIds = new ArrayList<>();
        List<Integer> rawCommIds = new ArrayList<>();

        //outputs
        List<Trader> traderEnts = new ArrayList<>();
        //Map<String, Trader> traderEnts = new HashMap<>();
        List<Trade> tradeEnts = new ArrayList<>();
        List<Comm> commEnts = new ArrayList<>();


        if (rawTrades.size() == 0 || rawComms.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }


        long maxTimestamp = -1;
        for (RawComm rawComm : rawComms) {
            rawCommIds.add(rawComm.getId());

            String[] parts = rawComm.getRaw().split(",");

            long time = parseTimestamp(parts[0]);
            String[] recipients = parts[2].split(";");

            traderEnts.add(Trader.parseRaw(parts[1]));
            //traderEnts.put(parts[1], Trader.parseRaw(parts[1]));

            for (String rec : recipients) {
                //traderEnts.put(rec, Trader.parseRaw(rec));
                traderEnts.add(Trader.parseRaw(rec));
            }

            for (String rec : recipients) {
                commEnts.add(new Comm(time, parts[1], rec));
            }

            if (maxTimestamp < time) {
                maxTimestamp = time;
            }
        }

        for (RawTrade raw : rawTrades) {
            rawTradeIds.add(raw.getId());

            Trade trade;

            try {
                trade = parseTrade(raw.getRaw());
            } catch (ParseException e) {
                return;
            }

            Trader buyer = Trader.parseRaw(trade.getBuyer());
            Trader seller = Trader.parseRaw(trade.getSeller());
            //Sector sector = new Sector(trade.getSector());
            //Symbol symbol = new Symbol(trade.getSymbol(), trade.getSector());

            //traderEnts.put(buyer.getEmail(), buyer);
            //traderEnts.put(seller.getEmail(), seller);
            traderEnts.add(buyer);
            traderEnts.add(seller);
            //sectorEnts.add(sector);
            //symbolEnts.add(symbol);

            tradeEnts.add(trade);

            if (maxTimestamp < trade.getTime()) {
                maxTimestamp = trade.getTime();
            }
        }


        //persist entities
        traderDao.insert(traderEnts);
        //traderDao.insert(traderEnts.values());
        commDao.insert(commEnts);
        //sectorDao.insert(sectorEnts);
        //symbolDao.insert(symbolEnts);
        tradeDao.insert(tradeEnts);

        System.out.println("Ids: " + AbstractDao.toList(rawTradeIds));

        rawCommDao.delete(rawCommIds);
        rawTradeDao.delete(rawTradeIds);

        trades += rawTrades.size();
        timeTaken += (System.nanoTime() - started);

        //System.out.println("Elapsed: " + (System.nanoTime() - started));
        //System.out.println("Elapsed/trade: " + (System.nanoTime() - started)/ents.size());
        System.out.println("Trades: " + trades + " Avg: " + (timeTaken / trades));

        Long maxTimeComplete = jdbcTemplate.queryForObject("SELECT max(time) FROM TimeInterval", Long.class);

        long roundedTime = roundToMinute(maxTimestamp);

        System.out.println("Max time: " + maxTimestamp + " rounded: " + roundToMinute(maxTimestamp));

        if (maxTimeComplete == null || roundedTime > maxTimeComplete) {
            jdbcTemplate.update("INSERT INTO TimeInterval (time) VALUES (?)", roundedTime);
        }
    }

    public static final int TIME_INTERVAL = 60*1000;
    public Long roundToMinute(Long time) {
        long over = time % TIME_INTERVAL;

        return (time - over);
    }




    /**
     * For the moment it strips precision down to milliseconds because:
     * - it's what SDF supports
     * - the extra digits of precision play no role in our system
     *
     * @param raw
     * @return
     * @throws java.text.ParseException
     */
    public Trade parseTrade(String raw) throws ParseException {
        String[] parts = raw.split(",");

        return new Trade(
                parseTimestamp(parts[0]),
                parts[1],
                parts[2],
                Float.parseFloat(parts[3]),
                Integer.parseInt(parts[4]),
                parts[5],
                parts[6],
                parts[7],
                Float.parseFloat(parts[8]),
                Float.parseFloat(parts[9])
        );
    }
}
