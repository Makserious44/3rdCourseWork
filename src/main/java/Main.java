import server.ConnectionHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ConnectionHandler handler = null;

        try {
            handler = new ConnectionHandler();
        }
        catch (IOException e) {
            System.out.println("Couldn't initialize connection handling: " + e.getMessage());
            return;
        }

        handler.handle();
    }
}
