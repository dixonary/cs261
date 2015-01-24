package team16.cs261.module.impl;

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
public class TradeAnalyser extends Module {

    BufferedReader reader;

    public TradeAnalyser(Config config) throws IOException {
        super(config, "TRADE-ANALYSER");

        Path path = Paths.get(config.getStoragePath(), "trades");
        Path dayPath = path.resolve("2015-01-22.csv");
        //Path dayPath = path;

        reader = Files.newBufferedReader(dayPath, Charset.defaultCharset());

    }

    @Override
    public void run() {

    }
}
