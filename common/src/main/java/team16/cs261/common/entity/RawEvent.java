package team16.cs261.common.entity;

/**
 * Created by martin on 24/02/15.
 */
public class RawEvent {

    int id;
    Type type;
    long time;
    String raw;

    public RawEvent(Type type, long time, String raw) {
        this.type = type;
        this.time = time;
        this.raw = raw;
    }

    public RawEvent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public enum Type {
        TRADE("trades"), COMM("comms");

        String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
