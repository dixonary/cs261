package team16.cs261.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.backend.module.Module;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class Backend {

    @Autowired
    Config config;

    ArrayList<Module> modules;

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public Backend() throws IOException {
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
