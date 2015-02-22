package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class RawComm {

    private int id;

    private String raw;

    public RawComm() {
    }

    public RawComm(String raw) {
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
