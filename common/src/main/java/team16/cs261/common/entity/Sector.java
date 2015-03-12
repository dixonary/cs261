package team16.cs261.common.entity;

/**
 * Created by martin on 14/02/15.
 */
public class Sector {

    private int id;
    private String sector;
    private int totalTrades;

    private int avg1, avg2, avg3;


    public Sector() {
    }

    public Sector(String sector) {
        this.sector = sector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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
