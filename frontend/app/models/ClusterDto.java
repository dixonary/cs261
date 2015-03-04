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
    private long time;

    public Cluster cluster;
    public List<Factor> factors;

    @QueryProjection
    public ClusterDto(int id, int tick, long time) {
        this.id = id;
        this.tick = tick;
        this.time=time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
