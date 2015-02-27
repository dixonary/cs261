package team16.cs261.backend.module;

import org.springframework.beans.factory.annotation.Autowired;
import team16.cs261.backend.Config;
import team16.cs261.backend.Counter;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.entity.RawEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */
public abstract class ReaderModule extends Module {

    private final RawEvent.Type type;
    private final String key;


    @Autowired
    protected RawEventDao rawEventDao;

    @Autowired
    PropDao props;


    protected List<RawEvent> rawEvents = new ArrayList<>();

    Counter items = new Counter();

    boolean seenHeaders = false;

    BufferedReader reader;


    public ReaderModule(Config config, String name, RawEvent.Type type, String key) {
        super(config, name);
        this.type = type;
        this.key = key;
    }

    public abstract BufferedReader getReader();

    //public Integer getPosition() {
    //  return props.getProperty("")
    //}

    @Override
    public void run() {
        try {
            this.reader = getReader();
            //BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

            String in;
            while ((in = reader.readLine()) != null) {

                if (!seenHeaders) {
                    seenHeaders = true;
                    continue;
                }

                onLine(in);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        onClose();
    }

    public void onLine(String in) throws IOException {
        //logger.info(in);
        //log(in);
        //if (true) return;

        long time = parseTimestamp(in.split(",")[0]);
        rawEvents.add(new RawEvent(type, time, in));
        if (type== RawEvent.Type.COMM && rawEvents.size() >= 10 ||
                type == RawEvent.Type.TRADE && rawEvents.size() >= 100) {
            persist();
        }

        items.increment();
        props.setProperty("input." + key + ".count", items.getCount());
        props.setProperty("input." + key + ".rate", items.getRate(15));
    }

    public void onClose() {
        persist();
    }

    public void persist() {
        rawEventDao.insert(rawEvents);
        rawEvents.clear();
    }
}



