package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionHandler {
    private final ServerSocket serverSocket;
    private final List<Socket> activeSockets = new ArrayList<>();

    public ConnectionHandler() throws IOException {
        try (InputStream propertiesFile = getClass().getResourceAsStream("/server.properties")) {
            Properties serverProperties = new Properties();
            serverProperties.load(propertiesFile);

            int serverPort = Integer.parseInt(serverProperties.getProperty("SERVER_PORT"));
            serverSocket = new ServerSocket(serverPort);
        }

        System.out.println("Connection handling initialized");
    }

    public void handle()  {
        System.out.println("Connection handler is up and running");

        while (true) {
            for (var socket : activeSockets) {
                if (socket.isClosed()) {
                    activeSockets.remove(socket);
                    continue;
                }
                System.out.println(socket.getInetAddress() + ":" + socket.getPort());
            }
//            activeSockets.removeIf(Socket::isClosed);

            try {
                Socket socket = serverSocket.accept();
                activeSockets.add(socket);

                System.out.println("Connected client: " + socket.getInetAddress() + ":" + socket.getPort());

                try {
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                }
                catch (IOException e) {
                    System.out.println("Couldn't initialize client handler: " + e.getMessage());
                }
            }
            catch (IOException e) {
                System.out.println("Couldn't establish connection with client: " + e.getMessage());
            }
        }
    }
}
