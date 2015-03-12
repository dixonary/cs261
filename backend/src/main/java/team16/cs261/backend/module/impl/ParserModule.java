package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.Module;
import team16.cs261.backend.util.Counter;
import team16.cs261.backend.util.Timer;
import team16.cs261.backend.util.Util;
import team16.cs261.common.dao.*;
import team16.cs261.common.entity.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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
    LogDao logDao;

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
        Timer selectRaw = new Timer("select raw");
        List<RawEvent> rawEvents = rawEventDao.selectAllLimit(1000);
        System.out.println(selectRaw.toString());

        List<Integer> rawEventIds = new ArrayList<>();

        if (rawEvents.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("Parsing...");
        Timer processTime = new Timer("Process time");


        //outputs
/*        List<Trader> traderEnts = new ArrayList<>();
        List<Symbol> symbolEnts = new ArrayList<>();
        List<Sector> sectorEnts = new ArrayList<>();*/
        Map<String, Trader> traderMap = new HashMap<>();
        Map<String, Symbol> symbolMap = new HashMap<>();
        Map<String, Sector> sectorMap = new HashMap<>();

        List<Trade> tradeEnts = new ArrayList<>();
        List<Comm> commEnts = new ArrayList<>();


        Set<Integer> ticks = new HashSet<>();
        int maxComm = -1;
        int maxTrade = -1;
        int maxTick;

        Timer parseTime = new Timer("Parse time");
        for (RawEvent rawEvent : rawEvents) {
            rawEventIds.add(rawEvent.getId());

            long time = rawEvent.getTime();
            int tick = Util.getTick(time, config.analysis.interval.length);
            ticks.add(tick);
            //maxTick = Math.max(maxTick, tick);

            switch (rawEvent.getType()) {
                case TRADE:
                    tradesCounter.increment();

                    maxTrade = Math.max(maxTrade, tick);

                    Trade trade;

                    try {
                        trade = parseTrade(time, rawEvent.getRaw());


/*                        Trader buyer = Trader.parseRaw(trade.getBuyer());
                        Trader seller = Trader.parseRaw(trade.getSeller());
                        Symbol symbol = new Symbol(trade.getSymbol(), trade.getSector());
                        Sector sector = new Sector(trade.getSector());

                        traderEnts.add(buyer);
                        traderEnts.add(seller);
                        symbolEnts.add(symbol);
                        sectorEnts.add(sector);*/

                        traderMap.put(trade.getBuyer(), Trader.parseRaw(trade.getBuyer()));
                        traderMap.put(trade.getSeller(), Trader.parseRaw(trade.getSeller()));
                        symbolMap.put(trade.getSymbol(), new Symbol(trade.getSymbol(), trade.getSector()));
                        sectorMap.put(trade.getSector(), new Sector(trade.getSector()));

                        tradeEnts.add(trade);
                    } catch (ParseException e) {
                        logDao.log("PARSER", "Failed to parse trade. Raw: '" + rawEvent.getRaw() + "', Exception: " + e.getMessage());
                        continue;
                    }

                    break;
                case COMM:
                    commsCounter.increment();

                    maxComm = Math.max(maxComm, tick);

                    try {
                        String[] parts = rawEvent.getRaw().split(",");

                        String sender = parts[1];

                        String[] recipients = parts[2].split(";");

                        //traderEnts.add(Trader.parseRaw(parts[1]));
                        traderMap.put(sender,Trader.parseRaw(sender) );

                        for (String rec : recipients) {
                            //traderEnts.add(Trader.parseRaw(rec));
                            traderMap.put(rec, Trader.parseRaw(rec));
                        }

                        for (String rec : recipients) {
                            commEnts.add(new Comm(time, tick, parts[0], parts[1], rec));
                        }

                    } catch (Exception e) {
                        logDao.log("PARSER", "Failed to parse comm. Raw: '" + rawEvent.getRaw() + "', Exception: " + e.getMessage());
                    }

                    break;
            }
        }
        System.out.println(parseTime);

        maxTick = Math.min(maxTrade, maxComm);

        Timer tickTime = new Timer("Tick time");
        List<Tick> tickEnts = new ArrayList<>();
        for (Integer t : ticks) {
            Tick tickEnt = new Tick(t, config.analysis.interval.length);
            tickEnts.add(tickEnt);

            //System.out.println("Tick: " + tickEnt);
        }

        tickDao.insert(tickEnts);

        System.out.println(tickTime);

        Collection<Trader> traderEnts = traderMap.values();
        Collection<Symbol> symbolEnts = symbolMap.values();
        Collection<Sector> sectorEnts = sectorMap.values();

        // persist nodes
        Timer insertNodes = new Timer("nodes insert");
        System.out.printf("tr: %d, sy: %d, se: %d\n", traderEnts.size(), symbolEnts.size(), sectorEnts.size());
        traderDao.insert(traderEnts);
        sectorDao.insert(sectorEnts);
        symbolDao.insert(symbolEnts);
        System.out.println(insertNodes + " nodes: " + traderEnts.size());

/*        Map<String, Trader> traderMap = traderDao.selectAsMap();
        Map<String, Symbol> symbolMap = symbolDao.selectAsMap();
        Map<String, Sector> sectorMap = sectorDao.selectAsMap();*/
        traderMap = traderDao.selectAsMap();
        symbolMap = symbolDao.selectAsMap();
        sectorMap = sectorDao.selectAsMap();

        for (Trade t : tradeEnts) {
            t.setBuyerId(traderMap.get(t.getBuyer()).getId());
            t.setSellerId(traderMap.get(t.getSeller()).getId());
            t.setSymbolId(symbolMap.get(t.getSymbol()).getId());
            t.setSectorId(sectorMap.get(t.getSector()).getId());
        }

        for (Comm c : commEnts) {
            c.setSenderId(traderMap.get(c.getSender()).getId());
            c.setRecipientId(traderMap.get(c.getRecipient()).getId());
        }


        //traderDao.insert(traderEnts.values());
        //sectorDao.insert(sectorEnts);
        //symbolDao.insert(symbolEnts);

        Timer insertTrades = new Timer("inserting trades");
        tradeDao.insert(tradeEnts);
        System.out.println(insertTrades + " trades: " + tradeEnts.size());

        Timer insertComms = new Timer("inserting comms");
        commDao.insert(commEnts);
        System.out.println(insertComms + " comms: " + commEnts.size());

        rawEventDao.delete(rawEventIds);


        jdbcTemplate.update("UPDATE Tick SET status = 'PARSED' WHERE status = 'UNPARSED' AND tick < ?", maxTick);


        // statistics

        //System.out.println("Rate: " + tradesCounter.getRate(60) + ", " + commsCounter.getRate(60));

        props.setProperty("parsed.trades.count", tradesCounter.getCount());
        props.setProperty("parsed.trades.rate", tradesCounter.getRate(60));
        props.setProperty("parsed.comms.count", commsCounter.getCount());
        props.setProperty("parsed.comms.rate", commsCounter.getRate(60));

        System.out.println(processTime);
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
                //toTick(time),
                Util.getTick(time, config.analysis.interval.length),
                parts[0],
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
