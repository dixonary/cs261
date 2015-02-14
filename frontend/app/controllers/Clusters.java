package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Result;
import team16.cs261.dal.dao.ClusterDao;
import team16.cs261.dal.dao.FactorDao;
import team16.cs261.dal.entity.Cluster;
import team16.cs261.dal.entity.factor.Factor;

import java.util.List;

import static play.mvc.Results.*;

@org.springframework.stereotype.Controller
public class Clusters {

    @Autowired
    ClusterDao clusterDao;

    @Autowired
    FactorDao factorDao;

    public Result index() {
        return ok(views.html.clusters.render());
    }

    public Result get(int clusterId) {
        Cluster clusterEnt = clusterDao.findById(clusterId);
        List<Factor> factorEnts = factorDao.findByClusterId(clusterId);

        if (clusterEnt == null) {
            return redirect(controllers.routes.Clusters.index());
        }

        return ok(views.html.cluster.render(clusterEnt, factorEnts));
    }
}