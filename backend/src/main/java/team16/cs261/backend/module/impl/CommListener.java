package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.Config;
import team16.cs261.dal.entity.Comm;
import team16.cs261.dal.entity.Trader;
import team16.cs261.backend.module.ListenerModule;
import team16.cs261.dal.dao.CommDao;
import team16.cs261.dal.dao.TraderDao;

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
public class CommListener extends ListenerModule {

    @Autowired
    TraderDao traderDao;

    @Autowired
    CommDao commDao;

    @Autowired
    public CommListener(Config config) throws IOException {
        super(config, "COMM-LISTENER", config.getHostname(), config.getCommsPort());
    }

    @Override
    public void onLine(String in) throws IOException {
        log(in);

        try {

            String[] parts = in.split(",");

            Date date = formatter.parse(parts[0].substring(0, 23));

            storeComm(date.getTime(), parts[1], parts[2].split(";"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void storeComm(long time, String sender, String[] recipients) {

        List<Trader> traderEnts = new ArrayList<>();

        traderEnts.add(Trader.parseRaw(sender));
        for (String rec : recipients) {
            traderEnts.add(Trader.parseRaw(rec));
        }

        traderDao.insert(traderEnts);

        List<Comm> commEnts = new ArrayList<>();
        for (String rec : recipients) {
            commEnts.add(new Comm(time, sender, rec));
        }

        commDao.insert(commEnts);

    }

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
    public static long parseTimestamp(String raw) throws ParseException {
        Date date = formatter.parse(raw.substring(0, 23));

        return date.getTime();
    }


}
