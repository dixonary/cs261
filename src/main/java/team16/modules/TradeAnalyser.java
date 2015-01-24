package team16.modules;

import team16.Config;
import team16.TcpListener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by martin on 22/01/15.
 */
public class TradeAnalyser implements Runnable {

    Config config;

    FileReader reader;

    public TradeAnalyser(Config config) throws IOException {
        this.config = config;

        Path path = Paths.get(config.getStoragePath(), "trades", "2015-01-22.csv");
        Path dayPath = path.resolve("2015-01-22.csv");
        //Path dayPath = path;


    }

    @Override
    public void run() {

    }
}
