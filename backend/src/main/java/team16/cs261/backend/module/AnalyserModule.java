package team16.cs261.backend.module;

import team16.cs261.backend.Config;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by martin on 22/01/15.
 */
public abstract class AnalyserModule extends Module {


    protected AnalyserModule(Config config, String name) {
        super(config, name);
    }

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Strips precision down to milliseconds because:
     * - it's what SDF supports
     * - the extra digits of precision play no role in our system
     *
     * @param raw
     * @return
     * @throws java.text.ParseException
     */

    public static long parseTimestamp(String raw) {
        try {
            Date date = formatter.parse(raw.substring(0, 23));
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
