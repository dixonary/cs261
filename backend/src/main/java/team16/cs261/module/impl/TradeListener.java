package team16.cs261.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.Config;
import team16.cs261.entity.Sector;
import team16.cs261.entity.Symbol;
import team16.cs261.entity.Trade;
import team16.cs261.entity.Trader;
import team16.cs261.dao.*;
import team16.cs261.module.ListenerModule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class TradeListener extends ListenerModule {

    @Autowired
    TraderDao traderDao;
    @Autowired
    TradeDao tradeDao;
    @Autowired
    SymbolDao symbolDao;
    @Autowired
    SectorDao sectorDao;


    @Autowired
    public TradeListener(Config config) throws IOException {
        super(config, "TRADE-LISTENER", config.getHostname(), config.getTradesPort());


    }

    @Override
    public void onLine(String in) throws IOException {

        log(in);

        Trade parsed = null;
        try {
            parsed = parseTrade(in);

            storeTrade(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Transactional
    public void storeTrade(Trade parsed) {


        List<Trader> traderEnts = new ArrayList<>();

        traderEnts.add(Trader.parseRaw(parsed.getBuyer()));
        traderEnts.add(Trader.parseRaw(parsed.getSeller()));


        Symbol symbol = new Symbol(parsed.getSymbol());
        Sector sector = new Sector(parsed.getSector());

        symbolDao.insert(symbol);
        sectorDao.insert(sector);

        traderDao.insertTraders(traderEnts);


        tradeDao.insertTrade(parsed);


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

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05
}
