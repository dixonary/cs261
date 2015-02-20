package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.dal.entity.*;
import team16.cs261.dal.dao.*;
import team16.cs261.backend.module.ListenerModule;

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
    RawTradeDao rawTrades;

    @Autowired
    public TradeListener(Config config) throws IOException {
        super(config, "TRADE-LISTENER", config.getHostname(), config.getTradesPort());
    }

    @Override
    public void onLine(String in) throws IOException {
        //log(in);

        rawTrades.insert(new RawTrade(in));
    }
}
