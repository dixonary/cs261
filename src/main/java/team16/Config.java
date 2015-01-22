package team16;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by martin on 22/01/15.
 */
public class Config {

    public static final String CONFIG_FILE = "config";

    private final String hostname;
    private final int tradesPort;
    private final int commsPort;

    public Config() throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(CONFIG_FILE));

        hostname = props.getProperty("HOSTNAME");
        tradesPort = Integer.parseInt(props.getProperty("TRADES_PORT"));
        commsPort = Integer.parseInt(props.getProperty("COMMS_PORT"));
    }

    public String getHostname() {
        return hostname;
    }

    public int getTradesPort() {
        return tradesPort;
    }

    public int getCommsPort() {
        return commsPort;
    }
}
