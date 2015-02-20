package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.AnalyserModule;
import team16.cs261.dal.dao.CommDao;
import team16.cs261.dal.dao.RawCommDao;
import team16.cs261.dal.dao.TradeDao;
import team16.cs261.dal.dao.TraderDao;
import team16.cs261.dal.entity.Comm;
import team16.cs261.dal.entity.RawComm;
import team16.cs261.dal.entity.Trader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class CommAnalyser extends AnalyserModule {

    @Autowired
    RawCommDao rawComms;

    @Autowired
    TradeDao tradeDao;

    @Autowired
    TraderDao traderDao;

    @Autowired
    CommDao commDao;

    @Autowired
    public CommAnalyser(Config config) throws IOException {
        super(config, "COMM-ANALYSER");
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }

    @Transactional
    public void process() {
        List<RawComm> rawCommEnts = rawComms.selectAll();
        List<Integer> rawIds = new ArrayList<>();

        List<Trader> traderEnts = new ArrayList<>();
        List<Comm> commEnts = new ArrayList<>();

        for (RawComm rawComm : rawCommEnts) {
            rawIds.add(rawComm.getId());

            String[] parts = rawComm.getRaw().split(",");

            long time = parseTimestamp(parts[0]);
            String sender = parts[1];
            String[] recipients = parts[2].split(";");

            traderEnts.add(Trader.parseRaw(sender));
            for (String rec : recipients) {
                traderEnts.add(Trader.parseRaw(rec));
            }

            for (String rec : recipients) {
                commEnts.add(new Comm(time, sender, rec));
            }
        }

        traderDao.insert(traderEnts);
        commDao.insert(commEnts);

        rawComms.delete(rawIds);
    }



}
