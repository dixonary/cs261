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
public class CommAnalyser extends Module {

    BufferedReader reader;

    public CommAnalyser(Config config) throws IOException {
        super(config, "COMM-ANALYSER");

        Path path = Paths.get(config.getStoragePath(), "comms");
        Path dayPath = path.resolve("2015-01-22.csv");

        reader = Files.newBufferedReader(dayPath, Charset.defaultCharset());
    }

    @Override
    public void run() {

    }
}
