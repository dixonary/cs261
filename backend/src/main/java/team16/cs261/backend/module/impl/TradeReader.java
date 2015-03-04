package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.entity.RawEvent;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class TradeReader extends ReaderModule {

    private Log logger = LogFactory.getLog(TradeReader.class);

    @Autowired
    PropDao props;

    @Autowired
    public TradeReader(Config config) throws IOException {
        super(config, "TRADE-READER", RawEvent.Type.TRADE, "trades");
    }

    public boolean flipside = false;

    @Override
    public Socket getSocket() throws IOException {
        return new Socket(config.getHostname(), config.getTradesPort());
    }

    @Override
    public Path getFile() {
        return config.getTradesFile();
    }

    @Override
    public void onHeaders(String headers) throws IOException {
        String[] parts = headers.split(",");

        flipside = parts[4].equals("currency");

        System.out.println("flipside: " + flipside);
    }

    @Override
    public void onLine(int line, String in) throws IOException {
        if (flipside) {
            String[] parts = in.split(",");

            String temp = parts[4];
            parts[4] = parts[5];
            parts[5] = temp;

            in = join(parts, ",");
        }

        super.onLine(line, in);

    }
}
