package models.graph;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 03/03/15.
 */
public class NodeDto {

    public int id;

    public String label;

    public String group;

    public NodeDto() {
    }

    @QueryProjection
    public NodeDto(int id, String label) {
        this.id = id;
        this.label = label;
    }
}
