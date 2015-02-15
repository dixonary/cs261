package team16.cs261.dal.dao;

import org.springframework.stereotype.Component;
import team16.cs261.dal.entity.Cluster;

/**
 * Created by martin on 13/02/15.
 */

@Component
public class ClusterDao extends AbstractDao<Integer, Cluster> {


    private static final String SELECT = "SELECT * FROM Cluster";

    public ClusterDao() {
        super(Cluster.class);
    }


}
