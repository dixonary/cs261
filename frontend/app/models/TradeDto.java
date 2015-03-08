package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 07/03/15.
 */
public class TradeDto {

    private int id;
    private long time;
    private int tick;

    private int buyerId;
    private java.lang.String buyer;
    private int sellerId;
    private java.lang.String seller;

    private float price;
    private int size;
    private java.lang.String currency;

    private int symbolId;
    private java.lang.String symbol;

    private int sectorId;
    private java.lang.String sector;

    private float bid;
    private float ask;

    @QueryProjection

    public TradeDto(int id, long time, int tick, int buyerId, String buyer, int sellerId, String seller, float price, int size, String currency, int symbolId, String symbol, int sectorId, String sector, float bid, float ask) {
        this.id = id;
        this.time = time;
        this.tick = tick;
        this.buyerId = buyerId;
        this.buyer = buyer;
        this.sellerId = sellerId;
        this.seller = seller;
        this.price = price;
        this.size = size;
        this.currency = currency;
        this.symbolId = symbolId;
        this.symbol = symbol;
        this.sectorId = sectorId;
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

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
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

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
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
