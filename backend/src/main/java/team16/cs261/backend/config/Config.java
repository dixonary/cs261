package team16.cs261.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    private Input input = Input.SOCKET;
    private Path tradesFile;
    private Path commsFile;

    public final Analysis analysis;

    @Autowired
    public Config(Options options, Analysis analysis) {
        this.options = options;
        this.analysis = analysis;

        if (options.tradesFile != null && options.commsFile != null) {
            input = Input.FILE;
            tradesFile = Paths.get(options.tradesFile);
            commsFile = Paths.get(options.commsFile);


            System.out.println("Trades: " + tradesFile.toUri());
        }
    }


    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
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


    /**
     * Created by martin on 26/02/15.
     */
    public static enum Input {
        SOCKET,
        FILE
    }

    /**
     * Created by martin on 02/03/15.
     */

    @Component
    public static class Analysis {

        public final Interval interval;
        @Autowired
        public Analysis(Interval interval) {
            this.interval = interval;
        }
    }

    @Component
    public static class Interval {
        public final int length;

        @Autowired
        public Interval(
                @Value("${analysis.interval.length}") int length
        ) {
            this.length=length;
        }
    }
}
