package team16.modules;

import team16.Config;
import team16.TcpListener;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by martin on 22/01/15.
 */
public class TradeListener {

    Config config;

    TcpListener tradeListener;

    FileWriter fw;

    public TradeListener(Config config) throws IOException {
        this.config = config;

        Path path = Paths.get(config.getStoragePath(), "trades"/*, "2015-01-22.csv"*/);
        Path dayPath = path.resolve("2015-01-22.csv");
        //Path dayPath = path;

        if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(path);
        }

        if (!Files.exists(dayPath)) {

            Files.createFile(dayPath);
        }

        fw = new FileWriter(dayPath.toFile());

        tradeListener = new TcpListener(config.getHostname(), config.getTradesPort()) {
            @Override
            public void onLine(String in) throws IOException {


                fw.write(in);
                fw.flush();
            }
        };

        new Thread(tradeListener).start();
    }
}
