package team16;

import java.io.IOException;

/**
 * Created by martin on 22/01/15.
 */
public class Main {

    Config config;

    TcpListener tradesIn;
    TcpListener commsIn;

    public Main() {

        try {
            config = new Config();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tradesIn = new TcpListener(config.getHostname(), config.getTradesPort()) {
            @Override
            public void onLine(String in) {
                System.out.println("Trade: " + in);
            }
        };

        commsIn = new TcpListener(config.getHostname(), config.getCommsPort()) {
            @Override
            public void onLine(String in) {
                System.out.println("Comm: " + in);
            }
        };

        new Thread(tradesIn).start();
        new Thread(commsIn).start();
    }

    public static void main(String[] args) {
        new Main();
    }

}
