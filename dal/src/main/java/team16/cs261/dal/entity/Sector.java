package team16.cs261.dal.entity;

/**
 * Created by martin on 14/02/15.
 */
public class Sector {

    private String name;
    private int totalTrades;

    private int avg1, avg2, avg3;


    public Sector() {
    }

    public Sector(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    public int getAvg1() {
        return avg1;
    }

    public void setAvg1(int avg1) {
        this.avg1 = avg1;
    }

    public int getAvg2() {
        return avg2;
    }

    public void setAvg2(int avg2) {
        this.avg2 = avg2;
    }

    public int getAvg3() {
        return avg3;
    }

    public void setAvg3(int avg3) {
        this.avg3 = avg3;
    }
}
