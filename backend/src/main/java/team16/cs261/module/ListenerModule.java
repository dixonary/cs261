package team16.cs261.module;

import team16.cs261.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by martin on 22/01/15.
 */
public abstract class ListenerModule extends Module {

    private final String host;
    private final int port;

    Socket socket;

    public ListenerModule(Config config, String name, String host, int port) {
        super(config, name);
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(host, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String in;
            while ((in = reader.readLine()) != null) {
                onLine(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract void onLine(String in) throws IOException;
}
