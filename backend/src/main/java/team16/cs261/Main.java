package team16.cs261;

import team16.cs261.module.Module;
import team16.cs261.module.ListenerModule;
import team16.cs261.module.impl.CommAnalyser;
import team16.cs261.module.impl.CommListener;
import team16.cs261.module.impl.TradeAnalyser;
import team16.cs261.module.impl.TradeListener;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by martin on 22/01/15.
 */
public class Main {

    Config config;

    ListenerModule tradesIn;
    ListenerModule commsIn;

    Set<String> emails = new TreeSet<>();

    Module[] modules = new Module[4];
    TradeListener tradeListener;
    TradeAnalyser tradeAnalyser;
    CommListener commListener;
    CommAnalyser commAnalyser;

    public Main() throws IOException {
        System.out.println("Initializing...");

        try {
            config = new Config();
        } catch (IOException e) {
            e.printStackTrace();
        }

        modules = new Module[]{
                (tradeListener = new TradeListener(config)),
                (tradeAnalyser = new TradeAnalyser(config)),
                (commListener = new CommListener(config)),
                (commAnalyser = new CommAnalyser(config))
        };

        for (Module m : modules) {
            Thread t = new Thread(m);
            t.start();
            m.log("Module thread started");
            //System.out.println("[\t" + m.getName() + "\t] Module thread started.");
        }

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

    public static void main(String[] args) throws IOException {
        new Main();
    }

}
