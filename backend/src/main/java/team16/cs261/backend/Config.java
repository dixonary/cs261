package team16.cs261.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class Config {

    private final Options options;

    @Value("${stream.hostname}")
    private String hostname;
    @Value("${stream.trade.port}")
    private int tradesPort;
    @Value("${stream.comm.port}")
    private int commsPort;

    @Value("${analysis.time.interval}")
    private int timeInterval;

    @Value("${analysis.time.short}")
    private int timeShort;

    @Value("${analysis.time.long}")
    private int timeLong;

    enum Mode {
        LIVE,
        HISTORIC
    }

    private Mode mode = Mode.LIVE;
    private Path tradesFile;
    private Path commsFile;

    @Autowired
    public Config(Options options) {
        this.options=options;

        if(options.tradesFile != null && options.commsFile != null) {
            mode = Mode.HISTORIC;
            tradesFile = Paths.get(options.tradesFile);
            commsFile = Paths.get(options.commsFile);


            System.out.println("Trades: " + tradesFile.toUri());
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Path getTradesFile() {
        return tradesFile;
    }

    public void setTradesFile(Path tradesFile) {
        this.tradesFile = tradesFile;
    }

    public Path getCommsFile() {
        return commsFile;
    }

    public void setCommsFile(Path commsFile) {
        this.commsFile = commsFile;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getTimeShort() {
        return timeShort;
    }

    public void setTimeShort(int timeShort) {
        this.timeShort = timeShort;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }



    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getTradesPort() {
        return tradesPort;
    }

    public void setTradesPort(int tradesPort) {
        this.tradesPort = tradesPort;
    }

    public int getCommsPort() {
        return commsPort;
    }

    public void setCommsPort(int commsPort) {
        this.commsPort = commsPort;
    }
}
