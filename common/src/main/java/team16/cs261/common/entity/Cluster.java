package team16.cs261.common.entity;

/**
 * Created by martin on 24/01/15.
 */
public class Cluster {

    private int clusterId;

    private long time;

    public Cluster() {

    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
