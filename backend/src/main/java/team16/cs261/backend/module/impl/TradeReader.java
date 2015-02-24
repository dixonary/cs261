package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.Counter;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.dao.RawTradeDao;
import team16.cs261.common.entity.RawEvent;
import team16.cs261.common.entity.RawTrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class TradeReader extends ReaderModule {

    private Log logger = LogFactory.getLog(TradeReader.class);

    @Autowired
    RawTradeDao rawTradeDao;

    List<RawTrade> rawList = new ArrayList<>();


    @Autowired
    PropDao props;

    @Autowired
    public TradeReader(Config config) throws IOException {
        super(config, "TRADE-READER", config.getTradesFile());
    }


    public Counter items = new Counter();

    @Override
    public void onLine(String in) throws IOException {
        logger.info(in);
        //log(in);





/*        long time = System.currentTimeMillis();
        rawTradeDao.insert(new RawTrade(in));
        System.out.println("elasped: " + (System.currentTimeMillis() - time));*/

        /*rawList.add(new RawTrade(in));
        if (rawList.size() >= 1000) {
            rawTradeDao.insert(rawList);
            rawList.clear();
        }*/

        long time = parseTimestamp(in.split(",")[0]);
        rawEvents.add(new RawEvent(RawEvent.Type.TRADE, time, in));
        if (rawEvents.size() >= 1000) {
            rawEventDao.insert(rawEvents);
            rawEvents.clear();
        }


        items.increment();
        props.setProperty("input.trades.count", items.getCount());
        props.setProperty("input.trades.rate", items.getRate(15));


    }
}
