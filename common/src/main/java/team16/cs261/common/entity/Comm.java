package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Comm {

    private int id;
    private long time;
    private String sender;
    private String recipient;

    protected Comm() {

    }

    public Comm(long time, String sender, String recipient) {
        this.time = time;
        this.sender = sender;
        this.recipient = recipient;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Comm{" +
                "id=" + id +
                ", time=" + time +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    public static Comm parseRaw(String raw) {
        return new Comm();
    }
}
