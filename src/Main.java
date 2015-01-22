/**
 * Created by martin on 22/01/15.
 */
public class Main {

    public static void main(String[] args) {

        TcpListener l = new TcpListener("cs261.dcs.warwick.ac.uk", 80) {
            @Override
            public void onLine(String in) {
                System.out.println("In: " + in);
            }
        };

        new Thread(l).start();


    }

}
