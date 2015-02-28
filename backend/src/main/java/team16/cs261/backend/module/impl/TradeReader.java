package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.common.dao.PropDao;
import team16.cs261.common.entity.RawEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class TradeReader extends ReaderModule {

    private Log logger = LogFactory.getLog(TradeReader.class);

    @Autowired
    public TradeReader(Config config) throws IOException {
        super(config, "TRADE-READER", RawEvent.Type.TRADE, "trades");
    }

    public boolean flipside = false;

    @Override
    public void onHeaders(String headers) {
        String[] parts = headers.split(",");

        flipside = parts[4].equals("currency");

        System.out.println("flipside: "+ flipside);
    }

    @Override
    public void onLine(String in) throws IOException {
        if (flipside) {
            String[] parts = in.split(",");

            String temp = parts[4];
            parts[4] = parts[5];
            parts[5] = temp;

            in = join(parts, ",");
        }

        super.onLine(in);
    }



    @Override
    public BufferedReader getReader() {
        if (config.getInput() == Config.Input.SOCKET) {
            Socket socket;
            try {
                socket = new Socket(config.getHostname(), config.getTradesPort());
                return new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return Files.newBufferedReader(config.getTradesFile(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
