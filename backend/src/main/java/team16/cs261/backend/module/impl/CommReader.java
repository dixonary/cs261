package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.ReaderModule;
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
public class CommReader extends ReaderModule {

    private Log logger = LogFactory.getLog(CommReader.class);

    @Autowired
    public CommReader(Config config) throws IOException {
        super(config, "COMM-READER", RawEvent.Type.COMM, "comms");
    }



    @Override
    public BufferedReader getReader() {
        if (config.getInput() == Config.Input.SOCKET) {
            Socket socket;
            try {
                socket = new Socket(config.getHostname(), config.getCommsPort());
                return new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader reader = Files.newBufferedReader(config.getCommsFile(), Charset.defaultCharset());
                return reader;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public void onHeaders(String headers) {

    }
}
