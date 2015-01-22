/**
 * Created by martin on 22/01/15.
 */
public class Main {

    public static final String HOST = "cs261.dcs.warwick.ac.uk";
    public static final int TRADES_PORT = 80;
    public static final int COMMS_PORT = 1720;

    TcpListener tradesIn;
    TcpListener commsIn;

    public Main() {
        tradesIn = new TcpListener(HOST, TRADES_PORT) {
            @Override
            public void onLine(String in) {
                System.out.println("Trade: " + in);
            }
        };

        commsIn = new TcpListener(HOST, COMMS_PORT) {
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
