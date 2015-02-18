package team16.cs261.dal.entity;

/**
 * Created by martin on 24/01/15.
 */
public class RawTrade {

    private int id;

    private String raw;

    public RawTrade() {
    }

    public RawTrade(String raw) {
        this.raw = raw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
