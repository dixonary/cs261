package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import team16.cs261.backend.util.Counter;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.Module;
import team16.cs261.common.dao.*;
import team16.cs261.common.entity.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Service
public class ParserModule extends Module {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Autowired
    RawEventDao rawEventDao;

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
    TickDao tickDao;

    @Autowired
    PropDao props;


    @Autowired
    public ParserModule(Config config) throws IOException {
        super(config, "PARSER");
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }

    Counter tradesCounter = new Counter();
    Counter commsCounter = new Counter();

    //@Transactional
    public void process() {
        List<RawEvent> rawEvents = rawEventDao.selectAllLimit(100);
        List<Integer> rawEventIds = new ArrayList<>();

        if (rawEvents.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        //System.out.println("Parsing...");


        //outputs
        List<Trader> traderEnts = new ArrayList<>();
        //Map<String, Trader> traderEnts = new HashMap<>();
        List<Trade> tradeEnts = new ArrayList<>();
        List<Comm> commEnts = new ArrayList<>();

        long maxTime = -1;

        Set<Integer> ticks = new HashSet<>();
        int maxTick = -1;


        for (RawEvent rawEvent : rawEvents) {
            rawEventIds.add(rawEvent.getId());

            long time = rawEvent.getTime();
            int tick = toTick(time);

            ticks.add(tick);
            if (maxTick < tick) {
                maxTick = tick;
            }

            switch (rawEvent.getType()) {
                case COMM:
                    commsCounter.increment();


                    String[] parts = rawEvent.getRaw().split(",");
                    String[] recipients = parts[2].split(";");

                    traderEnts.add(Trader.parseRaw(parts[1]));
                    //traderEnts.put(parts[1], Trader.parseRaw(parts[1]));

                    for (String rec : recipients) {
                        //traderEnts.put(rec, Trader.parseRaw(rec));
                        traderEnts.add(Trader.parseRaw(rec));
                    }

                    for (String rec : recipients) {
                        commEnts.add(new Comm(time, tick, parts[1], rec));
                    }

                    break;
                case TRADE:
                    tradesCounter.increment();

                    Trade trade;

                    try {
                        trade = parseTrade(time, rawEvent.getRaw());
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

                    break;
            }
        }


        List<Tick> tickEnts = new ArrayList<>();

        for (Integer t : ticks) {
            Tick tickEnt = new Tick(t, config.analysis.interval.length);
            tickEnts.add(tickEnt);

            //System.out.println("Tick: " + tickEnt);
        }

        tickDao.insert(tickEnts);



/*        long currentTick = toTick(maxTime);

        Integer maxTick = jdbcTemplate.queryForObject("SELECT max(tick) FROM Tick", Integer.class);
        if (maxTick == null) {
            //jdbcTemplate.update("INSERT INTO Tick (tick) VALUES (?)", currentTick - 1);
            long tick = currentTick - 1;

            jdbcTemplate.update("CALL InsertTick (?, ?, ?)",
                    tick,
                    tick * config.getTimeInterval(),
                    (tick + 1) * config.getTimeInterval());
            return;
        }

        for (int tick = maxTick + 1; tick < currentTick; tick++) {
            //System.out.println("i: " + tick);
            //jdbcTemplate.update("INSERT INTO Tick (tick) VALUES (?)", i);
            jdbcTemplate.update("CALL InsertTick (?, ?, ?)",
                    tick,
                    tick * config.getTimeInterval(),
                    (tick + 1) * config.getTimeInterval());
        }*/


        //persist entities
        traderDao.insert(traderEnts);
        //traderDao.insert(traderEnts.values());
        commDao.insert(commEnts);
        //sectorDao.insert(sectorEnts);
        //symbolDao.insert(symbolEnts);
        tradeDao.insert(tradeEnts);

        //System.out.println("Ids: " + AbstractDao.toList(rawTradeIds));
        rawEventDao.delete(rawEventIds);


        jdbcTemplate.update("UPDATE Tick SET status = 'PARSED' WHERE status = 'UNPARSED' AND tick < ?", maxTick);


        // statistics

        //System.out.println("Rate: " + tradesCounter.getRate(60) + ", " + commsCounter.getRate(60));

        props.setProperty("parsed.trades.count", tradesCounter.getCount());
        props.setProperty("parsed.trades.rate", tradesCounter.getRate(60));
        props.setProperty("parsed.comms.count", commsCounter.getCount());
        props.setProperty("parsed.comms.rate", commsCounter.getRate(60));


    }

    public int toTick(long time) {
        long over = time % config.analysis.interval.length;
        return (int) ((time - over) / config.analysis.interval.length);
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
    public Trade parseTrade(long time, String raw) throws ParseException {
        String[] parts = raw.split(",");

        return new Trade(
                time,
                toTick(time),
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
