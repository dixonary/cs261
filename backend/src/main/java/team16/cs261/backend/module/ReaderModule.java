package team16.cs261.backend.module;

import org.springframework.beans.factory.annotation.Autowired;
import team16.cs261.backend.Config;
import team16.cs261.common.dao.RawEventDao;
import team16.cs261.common.entity.RawEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */
public abstract class ReaderModule extends Module {

    @Autowired
    protected
    RawEventDao rawEventDao;

    protected List<RawEvent> rawEvents = new ArrayList<>();


    private final Path path;
    boolean seenHeaders = false;

    public ReaderModule(Config config, String name, Path path) {
        super(config, name);
        this.path=path;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());

            String in;
            while ((in = reader.readLine()) != null) {

                if (!seenHeaders) {
                    seenHeaders = true;
                    continue;
                }

                onLine(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract void onLine(String in) throws IOException;
}
