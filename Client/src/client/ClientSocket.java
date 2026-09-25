package client;

import lombok.Getter;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientSocket {
    private static final ClientSocket SINGLE_INSTANCE = new ClientSocket();

    @Getter
    private static Socket socket;
    @Getter
    private BufferedReader in;
    @Getter
    private PrintWriter out;

    private ClientSocket() {
        try (var propertiesFile = getClass().getResourceAsStream("/server.properties")) {
            Properties serverProperties = new Properties();
            serverProperties.load(propertiesFile);

            socket = new Socket(serverProperties.getProperty("SERVER_IP"),
                                Integer.parseInt(serverProperties.getProperty("SERVER_PORT")));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Couldn't create client socket: " + e.getMessage());
        }
    }

    public static ClientSocket getInstance() { return SINGLE_INSTANCE; }
}
