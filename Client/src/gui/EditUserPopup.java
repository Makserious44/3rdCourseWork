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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class EditUserPopup {
    public TextField firstName;
    public TextField lastName;
    public ComboBox<UserRole> userRole;
    public Label errorMessageField;
    public TextField phone;
    public TextField mail;
    public TextField address;

    private static User userToModify;

    public static void showWindow(User user) throws IOException {
        Objects.requireNonNull(user, "User must be non null");
        userToModify = user;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/EditUserPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Edit user");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        firstName.setText(userToModify.getUserInfo().getFirstName());
        lastName.setText(userToModify.getUserInfo().getLastName());
        phone.setText(userToModify.getUserInfo().getPhone());
        mail.setText(userToModify.getUserInfo().getMail());
        address.setText(userToModify.getUserInfo().getAddress());

//        userRole.setItems(FXCollections.observableList(Arrays.stream(UserRole.values()).map(UserRole::name).toList()));
        userRole.setItems(FXCollections.observableList(Arrays.stream(UserRole.values()).toList()));
        userRole.getSelectionModel().select(userToModify.getRole());

        System.out.println(userToModify);
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) firstName.getScene().getWindow()).close();
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (
            userRole.getSelectionModel().isEmpty() ||
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

        userToModify.setRole(userRole.getValue());

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userToModify.getUserInfo().getId());
        userInfo.setFirstName(firstName.getText());
        userInfo.setLastName(lastName.getText());
        userInfo.setMail(mail.getText());
        userInfo.setPhone(phone.getText());
        userInfo.setAddress(address.getText());

        userToModify.setUserInfo(userInfo);

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.MODIFY_USER, new Gson().toJson(userToModify)));

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) firstName.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Couldn't modify user: " + e.getMessage());
            errorMessageField.setText("Failed to modify user");
        }
    }
}
