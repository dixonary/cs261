package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 07/03/15.
 */
public class SectorDto {

    private int id;
    private String sector;

    @QueryProjection

    public SectorDto(int id, String sector) {
        this.id = id;
        this.sector = sector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
