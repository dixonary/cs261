package models;

import team16.cs261.common.entity.Cluster;
import team16.cs261.common.entity.factor.Factor;

import java.util.List;

/**
 * Created by martin on 15/02/15.
 */
public class ClusterDto {

    public Cluster cluster;
    public List<Factor> factors;

    public ClusterDto(Cluster cluster, List<Factor> factors) {
        this.cluster = cluster;
        this.factors = factors;
    }
}
