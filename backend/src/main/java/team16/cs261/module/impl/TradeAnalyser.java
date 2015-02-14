package team16.cs261.module.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.Config;
import team16.cs261.module.Module;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
