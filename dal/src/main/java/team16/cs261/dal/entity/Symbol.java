package team16.cs261.dal.entity;

/**
 * Created by martin on 14/02/15.
 */
public class Symbol {

    private String name;
    private String sector;
    private float price;
    private int totalTrades;

    private int tAvg1, tAvg2, tAvg3, pAvg1, pAvg2, pAvg3;

    public Symbol() {
    }

    public Symbol(String name, String sector) {
        this.name = name;
        this.sector = sector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    public int gettAvg1() {
        return tAvg1;
    }

    public void settAvg1(int tAvg1) {
        this.tAvg1 = tAvg1;
    }

    public int gettAvg2() {
        return tAvg2;
    }

    public void settAvg2(int tAvg2) {
        this.tAvg2 = tAvg2;
    }

    public int gettAvg3() {
        return tAvg3;
    }

    public void settAvg3(int tAvg3) {
        this.tAvg3 = tAvg3;
    }

    public int getpAvg1() {
        return pAvg1;
    }

    public void setpAvg1(int pAvg1) {
        this.pAvg1 = pAvg1;
    }

    public int getpAvg2() {
        return pAvg2;
    }

    public void setpAvg2(int pAvg2) {
        this.pAvg2 = pAvg2;
    }

    public int getpAvg3() {
        return pAvg3;
    }

    public void setpAvg3(int pAvg3) {
        this.pAvg3 = pAvg3;
    }
}
