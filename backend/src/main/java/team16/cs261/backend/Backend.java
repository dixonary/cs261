package team16.cs261.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.module.Module;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class Backend {

    @Autowired
    Config config;

    private final List<Module> modules;

    @Autowired
    public Backend(List<Module> modules) throws IOException {
        this.modules=modules;
    }


    @PostConstruct
    public void init() {
        System.out.println("Initializing...");

        System.out.println("Cfg: " + config.getHostname() + ", " + config.getCommsPort());

        for (Module m : modules) {
            Thread t = new Thread(m);
            t.start();
            m.log("Module thread started");
        }
    }
}
