package team16.cs261.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.backend.util.Counts;
import team16.cs261.common.dao.CountsDao;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.entity.RawEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by martin on 10/03/15.
 */

@Service
public class ReaderStateService {

    @Autowired
    Config config;

    @Autowired
    RawEventDao rawEventDao;

    @Autowired
    PropDao props;

    @Autowired
    CountsDao countsDao;

    @Autowired
    JdbcTemplate jdbc;


    @Transactional
    //public void saveReaderState(List<RawEvent> rawEvents, RawEvent.Type type) {
    public void saveReaderState(ReaderModule reader) {
        System.out.println("Persisting raw events: " + reader.type.getKey());

        List<RawEvent> rawEvents = reader.getRawEvents();
        Counts counts = reader.items2;

        //log("Persisting raw events");

        rawEventDao.insert(rawEvents);

        //System.out.println("Persisted up to line: " + line);
        if (config.getInput() == Config.Input.FILE)
            props.setProperty(reader.lineProp, reader.line);

        rawEvents.clear();


        String field = reader.type.getKey() + "Read";
        //String updateCounts = "UPDATE Counts SET "+field+" = ? WHERE intvl = ?";
        String updateCounts =
                "INSERT INTO Counts (intvl, time, sealed, " + field + ") VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY " +
                        "UPDATE " + field + " = VALUES(" + field + ")";

        Integer lastSealed = countsDao.lastSealed();
        int i = lastSealed != null ? Math.max(lastSealed, counts.epochIntvl) + 1 : counts.epochIntvl;

        //

        List<Object[]> args = new ArrayList<>();

        for (; i <= counts.getInterval(); i++) {

            boolean sealed = i < counts.getSealedInterval();

            Object[] a = new Object[]{
                    i,
                    counts.getRealtime(i),
                    sealed,
                    counts.getCount(i)
            };
            args.add(a);

            //System.out.printf("i: %s\n", Arrays.toString(a));
        }

        jdbc.batchUpdate(updateCounts, args);

        String sealPast = "UPDATE Counts SET sealed = true WHERE ? <= intvl AND intvl < ?";
        //jdbc.update(sealPast, items2.epochIntvl, items2.getSealedInterval());


        //items2.getCount()



    }

}
