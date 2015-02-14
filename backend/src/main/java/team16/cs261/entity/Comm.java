package team16.cs261.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Comm {

    private int id;
    private long timestamp;
    private String sender;
    private String recipient;

    protected Comm() {

    }

    public Comm(long timestamp, String sender, String recipient) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.recipient = recipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                ", timestamp=" + timestamp +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    public static Comm parseRaw(String raw) {
        return new Comm();
    }
}
