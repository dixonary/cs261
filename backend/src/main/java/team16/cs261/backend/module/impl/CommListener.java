package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.ListenerModule;
import team16.cs261.dal.dao.RawCommDao;
import team16.cs261.dal.entity.RawComm;

import java.io.IOException;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class CommListener extends ListenerModule {

    @Autowired
    RawCommDao rawComms;

    @Autowired
    public CommListener(Config config) throws IOException {
        super(config, "COMM-LISTENER", config.getHostname(), config.getCommsPort());
    }

    @Override
    public void onLine(String in) throws IOException {
        //log(in);

        rawComms.insert(new RawComm((in)));
    }
}
