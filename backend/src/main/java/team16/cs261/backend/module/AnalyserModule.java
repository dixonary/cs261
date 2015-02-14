package team16.cs261.backend.module;

import team16.cs261.backend.Config;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by martin on 22/01/15.
 */
public abstract class AnalyserModule extends Module {


    public AnalyserModule(Config config, String name, Path path) {
        super(config, name);


    }

    @Override
    public void run() {


    }

    public abstract void onLine(String in) throws IOException;
}
