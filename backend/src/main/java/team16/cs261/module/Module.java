package team16.cs261.module;

import team16.cs261.Config;

/**
 * Created by martin on 24/01/15.
 */
public abstract class Module implements Runnable{

    protected final Config config;

    private final String name;

    public Module(Config config, String name) {
        this.config = config;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void log(String line) {
        System.out.println("[\t" + name + "\t] " + line);
    }
}
