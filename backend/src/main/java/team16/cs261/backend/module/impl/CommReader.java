package team16.cs261.backend.module.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.ReaderModule;
import team16.cs261.common.entity.RawEvent;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

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
    public Socket getSocket() throws IOException {
        return new Socket(config.getHostname(), config.getCommsPort());
    }

    @Override
    public Path getFile() {
        return config.getCommsFile();
    }
}
