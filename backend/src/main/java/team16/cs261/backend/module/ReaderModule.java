package team16.cs261.backend.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.service.ReaderStateService;
import team16.cs261.backend.util.Counter;
import team16.cs261.backend.util.Counts;
import team16.cs261.common.dao.CountsDao;
import team16.cs261.common.dao.LogDao;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.entity.RawEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */
public abstract class ReaderModule extends Module {

    public final RawEvent.Type type;


    @Autowired
    protected RawEventDao rawEventDao;

    @Autowired
    PropDao props;

    @Autowired
    LogDao logDao;

    protected List<RawEvent> rawEvents = new ArrayList<>();

    Counter items = new Counter();
    public Counts items2 = new Counts();

    boolean seenHeaders = false;

    public BufferedReader reader;
    public int line;
    public int lastLine;

    private final String countProp;
    private final String rateProp;
    public final String lineProp;

    @Autowired
    ReaderStateService eventService;

    public ReaderModule(Config config, String name, RawEvent.Type type) {
        super(config, name);
        this.type = type;

        countProp = "input." + type.getKey() + ".count";
        rateProp = "input." + type.getKey() + ".rate";
        lineProp = "input." + type.getKey() + ".line";
    }

    public abstract Socket getSocket() throws IOException;

    public abstract Path getFile();

    public BufferedReader getReader() {
        try {
            switch (config.getInput()) {
                case SOCKET:
                    Socket socket = getSocket();
                    return new BufferedReader(new InputStreamReader(socket.getInputStream()));
                case FILE:
                    return Files.newBufferedReader(getFile(), Charset.defaultCharset());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() {


        try {

            this.reader = getReader();

            if (config.getInput() == Config.Input.FILE) {
                lastLine = props.getProperty(lineProp, Integer.class, -1);
                if (lastLine != -1) {
                    log("Will resume at line: " + lastLine);
                }
            }

            //BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

            line = 0;
            String in;
            while ((in = reader.readLine()) != null) {

                if (!seenHeaders) {
                    onHeaders(in);
                    seenHeaders = true;


                    continue;
                }

                onLine(line, in);
                line++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        onClose();
    }

    public void onHeaders(String headers) throws IOException {

    }

    public void onLine(int line, String in) throws IOException {
        if (line <= lastLine) {
            return;
        }

        //log("Line: " + line);

        //logger.info(in);
        //log(in);
        //if (true) return;


        String rawTime = in.split(",")[0];
        long time;
        try {
            time = parseTimestamp(rawTime);
        } catch (ParseException e) {
            logDao.log("READER", "Could not parse event. Raw: '"+in+"', Exception: " + e.toString());
            return;
        }


        rawEvents.add(new RawEvent(type, time, in));
        //if (rawEvents.size() >= 100) {
        if (shouldPersist()) {
            //persist();
            eventService.saveReaderState(this);
            lastPersisted = System.currentTimeMillis();
        }

        items.increment();
        items2.increment();

        props.setProperty(countProp, items.getCount());
        props.setProperty(rateProp, items.getRate(15));
    }

    public void onClose() {
        eventService.saveReaderState(this);
    }


    public boolean shouldPersist() {
        int minimum = type == RawEvent.Type.TRADE ? 100 : 100;

        return rawEvents.size() >= minimum ||
                rawEvents.size() > 0 && (System.currentTimeMillis() - lastPersisted) > persistInterval;
    }


    @Autowired
    JdbcTemplate jdbc;
    @Autowired
    CountsDao countsDao;

    long lastPersisted = 0;
    long persistInterval = 10 * 1000;



    public static String join(String[] parts, String delim) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String str = parts[i];
            sb.append(str);

            if (i < parts.length - 1) {
                sb.append(delim);
            }
        }

        return sb.toString();
    }

    public List<RawEvent> getRawEvents() {
        return rawEvents;
    }
}



