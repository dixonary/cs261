package team16.cs261.dal.entity;

/**
 * Created by martin on 24/01/15.
 */
public class TraderPair {

    private int id;

    private String email1;
    private String email2;

    private int totalStocks;
    private int sAvg1;
    private int sAvg2;
    private int sAvg3;

    private int totalTrades;
    private int tAvg1;
    private int tAvg2;
    private int tAvg3;

    private int totalComms;
    private int cAvg1;
    private int cAvg2;
    private int cAvg3;

    public TraderPair() {

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public int getTotalStocks() {
        return totalStocks;
    }

    public void setTotalStocks(int totalStocks) {
        this.totalStocks = totalStocks;
    }

    public int getsAvg1() {
        return sAvg1;
    }

    public void setsAvg1(int sAvg1) {
        this.sAvg1 = sAvg1;
    }

    public int getsAvg2() {
        return sAvg2;
    }

    public void setsAvg2(int sAvg2) {
        this.sAvg2 = sAvg2;
    }

    public int getsAvg3() {
        return sAvg3;
    }

    public void setsAvg3(int sAvg3) {
        this.sAvg3 = sAvg3;
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

    public int getTotalComms() {
        return totalComms;
    }

    public void setTotalComms(int totalComms) {
        this.totalComms = totalComms;
    }

    public int getcAvg1() {
        return cAvg1;
    }

    public void setcAvg1(int cAvg1) {
        this.cAvg1 = cAvg1;
    }

    public int getcAvg2() {
        return cAvg2;
    }

    public void setcAvg2(int cAvg2) {
        this.cAvg2 = cAvg2;
    }

    public int getcAvg3() {
        return cAvg3;
    }

    public void setcAvg3(int cAvg3) {
        this.cAvg3 = cAvg3;
    }
}
