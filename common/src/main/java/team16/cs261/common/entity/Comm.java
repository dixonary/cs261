package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Comm {

    private int id;

    private long time;
    private int tick;
    private String timestamp;

    private String sender;
    private String recipient;

    private int senderId, recipientId;

    protected Comm() {

    }

    public Comm(long time, int tick, String timestamp, String sender, String recipient) {
        this.time = time;
        this.tick=tick;
        this.timestamp=timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }
}
