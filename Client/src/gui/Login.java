package gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.Request;
import client.RequestSequence;
import connection.Response;
import connection.RequestType;
import data.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Login {
    @FXML
    private PasswordField inputPassword;
    @FXML
    private TextField inputLogin;
    @FXML
    private Button buttonRegister;
    @FXML
    private Label errorMessageField;

    public static void switchWindow(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Login.class.getResource("/Login.fxml")));
        Scene newScene = new Scene(root);

        stage.setScene(newScene);
        stage.centerOnScreen();
    }

    public void loginPressed(ActionEvent action) {
        User loginUser = new User();

        if (inputLogin.getText().isEmpty() || inputPassword.getText().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            loginUser.setLogin(inputLogin.getText());
            loginUser.setPassword(Integer.parseInt(inputPassword.getText()));
        }
        catch (NumberFormatException e) {
            System.out.println("Incorrect data input");
        }

        try {
            Response loginConfirmed = RequestSequence.sendRequest(new Request(RequestType.LOGIN, new Gson().toJson(loginUser)));

            System.out.println("Response from server: " + loginConfirmed.getMessage());

            if (!loginConfirmed.isSuccess())
                errorMessageField.setText("No users found");
            else {
                List<User> loginUserList = RequestSequence.deserializeResponse(loginConfirmed, User.class);
                loginUser = loginUserList.get(0);
            }
        }
        catch (NullPointerException e) {
            System.out.println("Failed to send request: " + e.getMessage());
            errorMessageField.setText("No data received");
        }
        catch (IOException ignored) { }
        System.out.println(loginUser);

        System.out.println("foundUser: " + loginUser);

        try {
            switch (loginUser.getRole()) {
                case admin -> AdminMenu.switchWindow((Stage) inputLogin.getScene().getWindow(), loginUser);
                case order_manager -> OrderManagerMenu.switchWindow((Stage) inputLogin.getScene().getWindow(), loginUser);
                case supply_manager -> SupplyManagerMenu.switchWindow((Stage) inputLogin.getScene().getWindow(), loginUser);
                case client -> ClientMenu.switchWindow((Stage) inputLogin.getScene().getWindow(), loginUser);
                case partner -> SupplierMenu.switchWindow((Stage) inputLogin.getScene().getWindow(), loginUser);
            }
        }
        catch (IOException e) {
            System.out.println("Failed to switch window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerPressed(ActionEvent action) {
        try {
            Register.switchWindow((Stage) inputLogin.getScene().getWindow());
        }
        catch (IOException e) {
            System.out.println("Failed to switch window: " + e.getMessage());
        }
    }
}
