package models.graph;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 03/03/15.
 */
public class EdgeDto {

    public int id;
    public int from;
    public int to;

    public String label;
    public String title;

    public double value;
    public double length;

    public EdgeDto() {
    }

    @QueryProjection
    public EdgeDto(int id) {
        this.id=id;
    }
}
