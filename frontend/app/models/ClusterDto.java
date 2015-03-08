package models;

import com.mysema.query.annotations.QueryProjection;
import team16.cs261.common.entity.Cluster;
import team16.cs261.common.entity.factor.Factor;

import java.util.List;

/**
 * Created by martin on 15/02/15.
 */
public class ClusterDto {

    private int id;
    private int tick;
    private long start;
    private long end;

    public Cluster cluster;
    public List<Factor> factors;

    @QueryProjection
    public ClusterDto(int id, int tick, long start, long end) {
        this.id = id;
        this.tick = tick;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
