package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Trader {

    private int id;
    private String email;
    private String domain;

    public Trader() {

    }

    public Trader(String email, String domain) {
        this.email = email;
        this.domain = domain;
    }

   public static Trader parseRaw(String raw) {
        return new Trader(raw, raw.split("@")[1]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "email='" + email + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
