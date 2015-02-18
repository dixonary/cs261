package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
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

@Component
public class TradeAnalyser extends Module {

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
        List<RawTrade> ents = rawTrades.selectAll();

        Set<Trade> tradeEnts = new HashSet<>();
        List<Trader> traderEnts = new ArrayList<>();
        List<Symbol> symbolEnts = new ArrayList<>();
        List<Sector> sectorEnts = new ArrayList<>();

        for (RawTrade raw : ents) {
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

        Date date = formatter.parse(parts[0].substring(0, 23));

        return new Trade(
                date.getTime(),
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
