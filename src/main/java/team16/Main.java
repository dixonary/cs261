package team16;

import team16.modules.TradeListener;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by martin on 22/01/15.
 */
public class Main {

    Config config;

    TcpListener tradesIn;
    TcpListener commsIn;

    Set<String> emails = new TreeSet<>();

    TradeListener tradeListener;

    public Main() throws IOException {

        try {
            config = new Config();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tradeListener = new TradeListener(config);

        tradesIn = new TcpListener(config.getHostname(), config.getTradesPort()) {
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
        };

        new Thread(tradesIn).start();
        new Thread(commsIn).start();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

}
