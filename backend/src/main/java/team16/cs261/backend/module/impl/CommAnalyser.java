package team16.cs261.backend.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.Config;
import team16.cs261.backend.module.Module;

import java.io.IOException;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class CommAnalyser extends Module {

    @Autowired
    public CommAnalyser(Config config) throws IOException {
        super(config, "COMM-ANALYSER");


    }

    @Override
    public void run() {

    }
}
