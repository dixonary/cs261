package team16.cs261;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class Config {

    @Value("${stream.hostname}")
    private String hostname;
    @Value("${stream.trade.port}")
    private int tradesPort;
    @Value("${stream.comm.port}")
    private int commsPort;

    public Config() {

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
