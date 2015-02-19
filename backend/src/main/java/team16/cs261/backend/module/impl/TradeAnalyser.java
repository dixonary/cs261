package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.AnalyserModule;
import team16.cs261.backend.module.Module;
import team16.cs261.dal.dao.*;
import team16.cs261.dal.entity.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Component
public class TradeAnalyser extends AnalyserModule {

    @Autowired
    RawTradeDao rawTrades;

    @Autowired
    TraderDao traderDao;
    @Autowired
    TradeDao tradeDao;
    @Autowired
    SymbolDao symbolDao;
    @Autowired
    SectorDao sectorDao;


    @Autowired
    public TradeAnalyser(Config config) throws IOException {
        super(config, "TRADE-ANALYSER");
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }


    @Transactional
    public void process() {
        List<RawTrade> ents = rawTrades.selectAllLimit(100);
        List<Integer> rawIds = new ArrayList<>();

        Set<Trade> tradeEnts = new HashSet<>();
        List<Trader> traderEnts = new ArrayList<>();
        List<Symbol> symbolEnts = new ArrayList<>();
        List<Sector> sectorEnts = new ArrayList<>();

        for (RawTrade raw : ents) {
            rawIds.add(raw.getId());

            Trade trade;

            try {
                trade = parseTrade(raw.getRaw());
            } catch (ParseException e) {
                return;
            }

            Symbol symbol = new Symbol(trade.getSymbol());
            Sector sector = new Sector(trade.getSector());

            traderEnts.add(Trader.parseRaw(trade.getBuyer()));
            traderEnts.add(Trader.parseRaw(trade.getSeller()));

            tradeEnts.add(trade);
            symbolEnts.add(symbol);
            sectorEnts.add(sector);
        }

        traderDao.insert(traderEnts);

        symbolDao.insert(symbolEnts);
        sectorDao.insert(sectorEnts);

        tradeDao.insert(tradeEnts);

        System.out.println("Ids: " + AbstractDao.toList(rawIds));

        rawTrades.delete(rawIds);
    }



    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * For the moment it strips precision down to milliseconds because:
     * - it's what SDF supports
     * - the extra digits of precision play no role in our system
     *
     * @param raw
     * @return
     * @throws java.text.ParseException
     */
    public static Trade parseTrade(String raw) throws ParseException {
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
