package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 07/03/15.
 */
public class TraderDto {

    private int id;

    private String email;
    private String domain;

    @QueryProjection
    public TraderDto(int id, String email) {
        this.id = id;
        this.email = email;
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
}
