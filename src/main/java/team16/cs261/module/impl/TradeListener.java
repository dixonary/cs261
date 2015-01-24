package team16.cs261.module.impl;

import team16.cs261.Config;
import team16.cs261.module.ListenerModule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by martin on 22/01/15.
 */
public class TradeListener extends ListenerModule {

    BufferedWriter writer;

    public TradeListener(Config config) throws IOException {
        super(config, "TRADE-LISTENER", config.getHostname(), config.getTradesPort());

        Path path = Paths.get(config.getStoragePath(), "trades"/*, "2015-01-22.csv"*/);
        Path dayPath = path.resolve("2015-01-22.csv");

        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(path);
        }

        if (!Files.exists(dayPath)) {
            Files.createFile(dayPath);
        }

        writer = Files.newBufferedWriter(dayPath, Charset.defaultCharset());
    }

    @Override
    public void onLine(String in) throws IOException {
        log(in);

        writer.write(in);
        writer.flush();
    }
}
