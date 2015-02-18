package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.Module;
import team16.cs261.dal.dao.CommDao;
import team16.cs261.dal.dao.RawCommDao;
import team16.cs261.dal.dao.TradeDao;
import team16.cs261.dal.dao.TraderDao;
import team16.cs261.dal.entity.Comm;
import team16.cs261.dal.entity.RawComm;
import team16.cs261.dal.entity.Trader;

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
public class CommAnalyser extends Module {

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

        List<Trader> traderEnts = new ArrayList<>();
        List<Comm> commEnts = new ArrayList<>();

        for (RawComm rawComm : rawCommEnts) {
            String in = rawComm.getRaw();

            String[] parts = in.split(",");

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
    }




    /*@Override
    public void onLine(String in) throws IOException {
        log(in);

        try {



            storeComm(date.getTime(), parts[1], parts[2].split(";"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void storeComm(long time, String sender, String[] recipients) {



    }*/

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    /**
     * Strips precision down to milliseconds because:
     * - it's what SDF supports
     * - the extra digits of precision play no role in our system
     *
     * @param raw
     * @return
     * @throws java.text.ParseException
     */

    public static long parseTimestamp(String raw) {
        try {
            Date date = formatter.parse(raw.substring(0, 23));
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
