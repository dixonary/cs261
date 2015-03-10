package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Trade {

    private int id;

    private long time;
    private int tick;
    private String timestamp;
    private String buyer;
    private String seller;

    private float price;
    private int size;

    private String currency;
    private String symbol;
    private String sector;

    private float bid;
    private float ask;

    public Trade() {
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public Trade(long time, int tick, String timestamp, String buyer, String seller, float price, int size, String currency, String symbol, String sector, float bid, float ask) {
        this.time = time;
        this.tick=tick;
        this.timestamp=timestamp;

        this.buyer = buyer;
        this.seller = seller;
        this.price = price;
        this.size = size;
        this.currency = currency;
        this.symbol = symbol;
        this.sector = sector;
        this.bid = bid;
        this.ask = ask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public float getAsk() {
        return ask;
    }

    public void setAsk(float ask) {
        this.ask = ask;
    }
}
