package team16.cs261.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import team16.cs261.backend.module.ListenerModule;
import team16.cs261.backend.module.Module;
import team16.cs261.backend.module.ReaderModule;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
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

            if(m instanceof ListenerModule && config.getMode() != Config.Mode.LIVE) continue;
            if(m instanceof ReaderModule && config.getMode() != Config.Mode.HISTORIC) continue;

            Thread t = new Thread(m);
            t.start();
            m.log("Module thread started");
        }
    }
}
