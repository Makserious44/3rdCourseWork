package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import connection.Response;
import data.entities.User;
import data.entities.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Register {
    @FXML
    private TextField inputLogin;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField repeatPassword;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField mail;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private Label errorMessageField;

    public static void switchWindow(Stage stage) throws IOException{
        stage.centerOnScreen();

        Parent root = FXMLLoader.load(Objects.requireNonNull(Register.class.getResource("/Register.fxml")));
        Scene newScene = new Scene(root);

        stage.setScene(newScene);
    }

    public void registerPressed(ActionEvent action) {
        User registerUser = new User();

        if (
                inputPassword.getText().isEmpty() ||
                inputLogin.getText().isEmpty() ||
                firstName.getText().isEmpty() ||
                lastName.getText().isEmpty() ||
                mail.getText().isEmpty() ||
                phone.getText().isEmpty() ||
                address.getText().isEmpty()) {

            System.out.println("All fields must be filled");
            errorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            registerUser.setLogin(inputLogin.getText());
            registerUser.setPassword(Integer.parseInt(inputPassword.getText()));
        }
        catch (NumberFormatException e) {
            System.out.println("Failed to convert password: " + e.getMessage());
            errorMessageField.setText("Incorrect password input");
            return;
        }

        if (inputPassword.getText().compareTo(repeatPassword.getText()) != 0) {
            System.out.println("Passwords don't match");
            errorMessageField.setText("Passwords don't match");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(firstName.getText());
        userInfo.setLastName(lastName.getText());
        userInfo.setMail(mail.getText());
        userInfo.setPhone(phone.getText());
        userInfo.setAddress(address.getText());

        registerUser.setUserInfo(userInfo);

        errorMessageField.setText(registerUser(registerUser));
    }

    public void cancelPressed(ActionEvent action) {
        try {
            Login.switchWindow((Stage) inputLogin.getScene().getWindow());
        }
        catch (IOException e) {
            System.out.println("Failed to switch window: " + e.getMessage());
        }
    }

    private String registerUser(User newUser) {
        Response response = null;

        try {
            response = RequestSequence.sendRequest(new Request(RequestType.REGISTER, new Gson().toJson(newUser)));
        }
        catch (IOException e) {
            System.out.println("Failed to send register request");
            return "Failed to send register request";
        }

        if (response.isSuccess())
            return "";
        else
            return "User with this login already exists";
    }
}
