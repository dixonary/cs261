package team16.cs261.backend.module;

import team16.cs261.backend.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by martin on 24/01/15.
 */
public abstract class Module implements Runnable {

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


    public final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Strips precision down to milliseconds because:
     * - it's what SDF supports
     * - the extra digits of precision play no role in our system
     *
     * @param raw
     * @return
     * @throws java.text.ParseException
     */

    public long parseTimestamp(String raw) {
        try {
            Date date = formatter.parse(raw.substring(0, 23));
            return date.getTime();
        } catch (Exception e) {
            System.out.println("Failed parsing: '" +  raw +"'");
            e.printStackTrace();
            return -1;
        }
    }
}
