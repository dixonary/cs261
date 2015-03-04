package team16.cs261.backend.module;

import team16.cs261.backend.config.Config;

import java.text.ParseException;
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
        System.out.println("[\t\t" + name + "\t] " + line);
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

    public long parseTimestamp(String raw) throws ParseException {
        if (raw.length() < 23) {
            raw = raw + ".000";
        } else {
            raw = raw.substring(0, 23);
        }

        Date date = formatter.parse(raw);
        return date.getTime();
    }
}
