package team16.cs261.backend.util;

/**
 * Created by martin on 03/03/15.
 */
public class Timer {

    public final long epoch;
    public long elapsed = -1;

    public final String label;

    public Timer(String label) {
        this.epoch = System.currentTimeMillis();
        this.label = label;
    }

    public void stop() {
        elapsed = getElapsed();
    }

    public long getElapsed() {
        return elapsed != -1 ? elapsed : System.currentTimeMillis() - epoch;
    }

    @Override
    public String toString() {
        return label + ": " + getElapsed() + "ms";
    }
}
