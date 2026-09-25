package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.User;
import data.entities.UserInfo;
import data.enums.UserRole;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class CreateUserPopup {
//    private static CreateUserPopup createUserPopup = null;

    public TextField inputLogin;
    public PasswordField inputPassword;
    public TextField firstName;
    public TextField lastName;
    public TextField phone;
    public TextField mail;
    public TextField address;
    public Label errorMessageField;
    public ComboBox<UserRole> userRole;

//    public static User madeUser;

    public static void showWindow() throws IOException {
        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/CreateUserPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Create user");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
//        userRole.setItems(FXCollections.observableList(Arrays.stream(UserRole.values()).map(UserRole::name).toList()));
        userRole.setItems(FXCollections.observableList(Arrays.stream(UserRole.values()).toList()));
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        User registerUser = new User();

        if (
            userRole.getSelectionModel().isEmpty() ||
            inputPassword.getText().isEmpty() ||
            inputLogin.getText().isEmpty() ||
            firstName.getText().isEmpty() ||
            lastName.getText().isEmpty() ||
            mail.getText().isEmpty() ||
            phone.getText().isEmpty() ||
            address.getText().isEmpty()
        ) {

            System.out.println("All fields must be filled");
            errorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            registerUser.setLogin(inputLogin.getText());
            registerUser.setPassword(Integer.parseInt(inputPassword.getText()));
            System.out.println(userRole.getValue());
            registerUser.setRole(userRole.getValue());
        }
        catch (NumberFormatException e) {
            System.out.println("Failed to convert password: " + e.getMessage());
            errorMessageField.setText("Incorrect password input");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(firstName.getText());
        userInfo.setLastName(lastName.getText());
        userInfo.setMail(mail.getText());
        userInfo.setPhone(phone.getText());
        userInfo.setAddress(address.getText());

        registerUser.setUserInfo(userInfo);

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.REGISTER, new Gson().toJson(registerUser)));

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) inputLogin.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Couldn't register user: " + e.getMessage());
            errorMessageField.setText("Failed to register user");
        }
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) inputLogin.getScene().getWindow()).close();
    }
}
