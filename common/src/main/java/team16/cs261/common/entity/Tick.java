package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Tick {

    private long tick;
    private long start;
    private long end;

    // counts
    private int trades;
    private int comms;

    // rates
    private double tradesPerTrader;

    private double commonPerPair;
    private double commonBuysPerPair;
    private double commonSellsPerPair;

    private double commsPerTrader;
    private double commsPerPair;



    public Tick() {
    }

    public Tick(long tick, int interval) {
        this.tick = tick;
        this.start = tick * interval;
        this.end = (tick + 1) * interval;
    }


    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getTrades() {
        return trades;
    }

    public void setTrades(int trades) {
        this.trades = trades;
    }

    public int getComms() {
        return comms;
    }

    public void setComms(int comms) {
        this.comms = comms;
    }

    public double getTradesPerTrader() {
        return tradesPerTrader;
    }

    public void setTradesPerTrader(double tradesPerTrader) {
        this.tradesPerTrader = tradesPerTrader;
    }

    public double getCommonPerPair() {
        return commonPerPair;
    }

    public void setCommonPerPair(double commonPerPair) {
        this.commonPerPair = commonPerPair;
    }

    public double getCommonBuysPerPair() {
        return commonBuysPerPair;
    }

    public void setCommonBuysPerPair(double commonBuysPerPair) {
        this.commonBuysPerPair = commonBuysPerPair;
    }

    public double getCommonSellsPerPair() {
        return commonSellsPerPair;
    }

    public void setCommonSellsPerPair(double commonSellsPerPair) {
        this.commonSellsPerPair = commonSellsPerPair;
    }

    public double getCommsPerTrader() {
        return commsPerTrader;
    }

    public void setCommsPerTrader(double commsPerTrader) {
        this.commsPerTrader = commsPerTrader;
    }

    public double getCommsPerPair() {
        return commsPerPair;
    }

    public void setCommsPerPair(double commsPerPair) {
        this.commsPerPair = commsPerPair;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "tick=" + tick +
                ", start=" + start +
                ", end=" + end +
                ", trades=" + trades +
                ", comms=" + comms +
                ", tradesPerTrader=" + tradesPerTrader +
                ", commonPerPair=" + commonPerPair +
                ", commonBuysPerPair=" + commonBuysPerPair +
                ", commonSellsPerPair=" + commonSellsPerPair +
                ", commsPerTrader=" + commsPerTrader +
                ", commsPerPair=" + commsPerPair +
                '}';
    }
}
