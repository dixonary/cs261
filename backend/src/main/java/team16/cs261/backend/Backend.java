package team16.cs261.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team16.cs261.dal.dao.TraderDao;
import team16.cs261.dal.entity.Trader;
import team16.cs261.module.Module;
import team16.cs261.module.ListenerModule;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by martin on 22/01/15.
 */

@Component
public class Backend {

    @Autowired
    Config config;

    ListenerModule tradesIn;
    ListenerModule commsIn;

    Set<String> emails = new TreeSet<>();


    List<Module> modules;


    @Autowired
    TraderDao traderDao;


    public void setTraderDao(TraderDao traderDao) {
        this.traderDao = traderDao;
    }

    public Backend() throws IOException {
    }

    @PostConstruct
    public void init() {

        System.out.println("Init");

        System.out.println("Cfg: " + config.getHostname() + ", " + config.getCommsPort());

        /*try {
            traderDao.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        /*Trader trader = new Trader();
        trader.setEmail("bob@gmail.com");
        trader.setDomain("gmail.com");
        traderDao.insertTrader(trader);*/


        for (Module m : modules) {
            Thread t = new Thread(m);
            t.start();
            m.log("Module thread started");
        }


        List<Trader> traders = traderDao.getTraders();
        System.out.println("TradeR: " + traders);
    }

    public void init2() throws IOException {


        System.out.println("Initializing...");


/*
        modules = new Module[]{
                (tradeListener = new TradeListener(config)),
                (tradeAnalyser = new TradeAnalyser(config)),
                (commListener = new CommListener(config)),
                (commAnalyser = new CommAnalyser(config))
        };*/



    /*    tradesIn = new TcpListener(config.getHostname(), config.getTradesPort()) {
            @Override
            public void onLine(String in) {
                System.out.println("Trade: " + in);

                String e1 = in.split(",")[1];
                String e2 = in.split(",")[2];

                emails.add(e1);
                emails.add(e2);

                System.out.println("Emails: " + emails.size());
            }
        };


        commsIn = new TcpListener(config.getHostname(), config.getCommsPort()) {
            @Override
            public void onLine(String in) {


                String es = in.split(",")[1];
                String[] ers = in.split(",")[2].split(";");

                emails.add(es);
                Collections.addAll(emails, ers);

                System.out.println("Emails: " + emails.size());
            }
        };*/

        //new Thread(tradesIn).start();
        //new Thread(commsIn).start();
    }


    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
