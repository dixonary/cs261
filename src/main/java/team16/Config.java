package team16;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by martin on 22/01/15.
 */
public class Config {

    public static final String CONFIG_FILE = "config";

    Properties props = new Properties();

    public Config() throws IOException {
        props.load(new FileReader(CONFIG_FILE));
    }

    public String getHostname() {
        return props.getProperty("HOSTNAME");
    }

    public int getTradesPort() {
        return Integer.parseInt(props.getProperty("TRADES_PORT"));
    }

    public int getCommsPort() {
        return Integer.parseInt(props.getProperty("COMMS_PORT"));
    }


}
