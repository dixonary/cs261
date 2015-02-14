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
public class TradeAnalyser extends Module {

    @Autowired
    public TradeAnalyser(Config config) throws IOException {
        super(config, "TRADE-ANALYSER");


    }

    @Override
    public void run() {

    }
}
