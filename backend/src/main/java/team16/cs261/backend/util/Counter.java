package team16.cs261.backend.util;

import java.util.Random;

/**
 * Created by martin on 24/02/15.
 */
public class Counter {

    final long epoch;
    final int[] counts;

    int count;


    public static final Integer INTERVAL_SIZE = 1000; // second slots
    public static final int INTERVALS = 60; // maintain 60 intervals


    public Counter() {
        this.epoch = System.currentTimeMillis();
        this.counts = new int[INTERVALS];
    }

    int lastInt = 0;
    private int getInterval() {
        int interval =  (int) ((System.currentTimeMillis() - epoch) / INTERVAL_SIZE) % INTERVALS;

        if(interval > lastInt || interval == 0 && lastInt == INTERVALS-1) {
            counts[interval] = 0;
            lastInt = interval;
        }

        return interval;
    }

    public void increment(int n) {
        counts[getInterval()] += n;
        count += n;
    }

    public void increment() {
        increment(1);
    }



    public int getCount() {
        return count;
    }

    public float getRate(int n) {
        int now = getInterval();

        int start = now - n;

        float sum = 0;
        for (int i = start; i < now; i++) {
            int interval = ((i % INTERVALS) + INTERVALS) % INTERVALS;
            sum += counts[interval];
        }

        return sum / n;
    }
}
