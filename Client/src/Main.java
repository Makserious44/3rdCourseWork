import client.ClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ClientSocket.getInstance();

            Parent rootScene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));

//            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            primaryStage.setTitle("Store Stocker v0.1");
            primaryStage.setScene(new Scene(rootScene, 500, 300));
            primaryStage.show();
        }
        catch (IOException e) {
            System.out.println("Couldn't create window: " + e.getMessage());
        }

//        var userInterface = new UserInterface();
//        userInterface.run();
    }

    public static void main(String[] args) { launch(args); }
}
