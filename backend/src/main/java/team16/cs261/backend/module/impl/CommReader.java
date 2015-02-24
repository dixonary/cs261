package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.Counter;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.RawCommDao;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.entity.RawComm;
import team16.cs261.common.entity.RawEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class CommReader extends ReaderModule {

    private Log logger = LogFactory.getLog(TradeReader.class);


    @Autowired
    RawCommDao rawCommDao;

    List<RawComm> rawList = new ArrayList<>();




    @Autowired
    PropDao props;

    public Counter items = new Counter();

    @Autowired
    public CommReader(Config config) throws IOException {
        super(config, "COMM-READER", config.getCommsFile());
    }

    @Override
    public void onLine(String in) throws IOException {
        //log(in);
        logger.info(in);


/*        long time = System.currentTimeMillis();
        rawTradeDao.insert(new RawTrade(in));
        System.out.println("elasped: " + (System.currentTimeMillis() - time));*/

        /*rawList.add(new RawComm(in));
        if (rawList.size() >= 1000) {
            rawCommDao.insert(rawList);
            rawList.clear();
        }*/

        long time = parseTimestamp(in);
        rawEvents.add(new RawEvent(RawEvent.Type.COMM, time, in));
        if (rawEvents.size() >= 1000) {
            rawEventDao.insert(rawEvents);
            rawEvents.clear();
        }


        items.increment();
        props.setProperty("input.comms.count", items.getCount());
        props.setProperty("input.comms.rate", items.getRate(15));

    }
}
