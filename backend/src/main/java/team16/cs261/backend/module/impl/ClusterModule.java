package team16.cs261.backend.module.impl;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team16.cs261.backend.config.Config;
import team16.cs261.backend.mcl.Graph;
import team16.cs261.backend.mcl.Mcl;
import team16.cs261.backend.module.Module;
import team16.cs261.backend.service.MclService;
import team16.cs261.backend.util.Timer;
import team16.cs261.common.dao.*;
import team16.cs261.common.entity.Tick;
import team16.cs261.common.entity.Trade;
import team16.cs261.common.entity.graph.Edge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by martin on 22/01/15.
 */

// time,buyer,seller,price,size,currency,symbol,sector,bid,ask
// 2015-02-14 08:43:55.480228,w.hastings@bridgewater.com,a.clare@sorrel.com,925.50,27714,GBX,ARM.L,Technology,932.19,933.05

@Component
public class ClusterModule extends Module {

    @Autowired
            JdbcTemplate template;

    List<Future<String>> futures = new ArrayList<>();

    @Autowired
    MclService mclService;

    @Autowired
    public ClusterModule(Config config) throws IOException {
        super(config, "CLUSTERER");
    }

    public void init() {

    }

    @Override
    public void run() {

        while(true) {
            process();
        }

    }

    private void process() {
        //List<String> mclInputs = template.queryForList("SELECT mclInput FROM Tick WHERE status = 'ANALYSED'", String.class);

        SqlRowSet rows = template.queryForRowSet("SELECT tick, mclInput FROM Tick WHERE status = 'ANALYSED'");

        //Tick tick = jdbcTemplate.queryForObject("SELECT min(tick) FROM Tick WHERE status = 'PARSED'", Long.class);
        /*if (mclInputs.size()==0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }*/
/*
        while (rows.next()) {
            int tick = rows.getInt(1);
            String mclInput = rows.getString(2);

            System.out.println("Running tick: "+ tick);
            try {
                mclService.run(tick, mclInput);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Running tick222: "+ tick);
        }*/



        /*for(String mclInput : mclInputs) {
            System.out.println("in: " + mclInput);

            runMcl()
        }*/

    }
}
