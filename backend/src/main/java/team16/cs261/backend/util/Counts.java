package team16.cs261.backend.util;

/**
 * Created by martin on 24/02/15.
 */
public class Counts {

    private final long epoch;
    public final int epochIntvl;
    //public final int epoch;

    private final int[] counts;

    int lastInterval;

    int count;


    // interval size in ms
    public static final Integer INTERVAL_SIZE = 10 * 1000;
    public static final int INTERVALS = 365 * 24 * 60 * 6;


    public Counts() {
        epoch = System.currentTimeMillis();
        epochIntvl = getInterval(epoch);

        this.counts = new int[INTERVALS];
    }

    public static void main(String[] args) {
        Counts c = new Counts();

        System.out.printf("intvl: %d" , c.getInterval());
    }


    public long getRealtime(long intvl) {
        return intvl * INTERVAL_SIZE;
    }

    public int getInterval(long time) {
        return Util.getTick(time, INTERVAL_SIZE);
    }

    public int getInterval() {
        return getInterval(System.currentTimeMillis());
    }

    public int getSealedInterval() {
        return lastInterval;
    }

    public void increment(int n) {
        lastInterval = getInterval();

        counts[lastInterval-epochIntvl] += n;
        count += n;
    }

    public void increment() {
        increment(1);
    }

    public int getCount(int intvl) {
        return counts[intvl-epochIntvl];
    }

    public int getCount() {
        return count;
    }

    public float getRate(int n) {
        int now = getInterval();

        int start = now - n;

        float sum = 0;
        for (int i = start; i < now; i++) {
            int interval = i + INTERVALS;
            sum += counts[interval];
        }

        return sum / n;
    }



}
