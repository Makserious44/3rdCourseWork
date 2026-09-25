package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.*;
import gui.misc.TablePopulator;
import gui.misc.FactoryParam;
import gui.misc.TableCellValueFactorySetter;
import gui.misc.converters.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ClientMenu {
    public static User currentUser;

    public TextField inputLogin;
    public PasswordField inputPassword;
    public PasswordField repeatPassword;

    public TextField firstName;
    public TextField lastName;
    public TextField phone;
    public TextField mail;
    public TextField address;

    public Label settingsErrorMessageField;
    public Label requestErrorMessageField;
    public Label orderErrorMessageField;

    @FXML
    private TableView<RequestTableEntry> requestsTable;

    @FXML
    private TableView<OrderTableEntry> ordersTable;

    public static void switchWindow(Stage stage, User user) throws IOException {
        Objects.requireNonNull(user, "User cannot be null");
        currentUser = user;

        Objects.requireNonNull(user, "Received null user");
        stage.centerOnScreen();

        Parent root = FXMLLoader.load(Objects.requireNonNull(AdminMenu.class.getResource("/ClientMenu.fxml")));
        Scene newScene = new Scene(root);

        stage.setScene(newScene);

    }

    @FXML
    public void initialize() {
        new TableCellValueFactorySetter<>(requestsTable).setValueFactories(List.of(
                new FactoryParam(Integer.class, "id"),
                new FactoryParam(Integer.class, "userId"),
                new FactoryParam(Map.class, "productNames")
        ));

        new TableCellValueFactorySetter<>(ordersTable).setValueFactories(List.of(
                new FactoryParam(Integer.class, "id"),
                new FactoryParam(Integer.class, "ordererId"),
                new FactoryParam(Integer.class, "supplierId"),
                new FactoryParam(Map.class, "productNames")
        ));

        TablePopulator.populateTable(requestsTable, RequestType.GET_OWN_REQUESTS, currentUser, data.entities.Request.class, RequestTableEntry.converter);
        TablePopulator.populateTable(ordersTable, RequestType.GET_OWN_ORDERS, currentUser, Order.class, OrderTableEntry.converter);

        inputLogin.setText(currentUser.getLogin());
        inputPassword.setText(String.valueOf(currentUser.getPassword()));
        firstName.setText(currentUser.getUserInfo().getFirstName());
        lastName.setText(currentUser.getUserInfo().getLastName());
        phone.setText(currentUser.getUserInfo().getPhone());
        mail.setText(currentUser.getUserInfo().getMail());
        address.setText(currentUser.getUserInfo().getAddress());
    }

    // ---- REQUEST TABLE ----

    public void onAddRequestPressed(ActionEvent actionEvent) {
        try {
            CreateRequestPopup.showWindow(currentUser);
        }
        catch (IOException e) {
            System.out.println("Couldn't show window");
        }
    }

    public void onEditRequestPressed(ActionEvent actionEvent) {
        try {
            var entry = requestsTable.getSelectionModel().getSelectedItem().getRequest();
            if (entry == null)
                return;
            EditRequestPopup.showWindow(entry);
        }
        catch (IOException e) {
            System.out.println("Couldn't show window    ");
        }
    }

    public void onRemoveRequestPressed(ActionEvent actionEvent) {
        RequestTableEntry entry = requestsTable.getSelectionModel().getSelectedItem();

        if (entry == null) {
            return;
        }

        try {
            var response = RequestSequence.sendRequest(
                    new Request(RequestType.REMOVE_REQUEST, new Gson().toJson(entry.getRequest()))
            );
            if (!response.isSuccess())
                requestErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to remove Request");
        }
    }

    public void onRequestRefreshedPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(requestsTable, RequestType.GET_OWN_REQUESTS, currentUser, data.entities.Request.class, RequestTableEntry.converter);
    }

    // ---- Order ----

    public void onOrderRefreshedPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(ordersTable, RequestType.GET_OWN_ORDERS, currentUser, Order.class, OrderTableEntry.converter);
    }

    // ---- SETTINGS ----

    public void onSaveSettingsChangesPressed(ActionEvent actionEvent) {
        User modifyUser = new User();

        if (
                inputPassword.getText().isEmpty() ||
                        inputLogin.getText().isEmpty() ||
                        firstName.getText().isEmpty() ||
                        lastName.getText().isEmpty() ||
                        mail.getText().isEmpty() ||
                        phone.getText().isEmpty() ||
                        address.getText().isEmpty()) {

            System.out.println("All fields must be filled");
            settingsErrorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            modifyUser.setId(currentUser.getId());
            modifyUser.setRole(currentUser.getRole());
            modifyUser.setLogin(inputLogin.getText());
            modifyUser.setPassword(Integer.parseInt(inputPassword.getText()));
        }
        catch (NumberFormatException e) {
            System.out.println("Failed to convert password: " + e.getMessage());
            settingsErrorMessageField.setText("Incorrect password input");
            return;
        }

        if (inputPassword.getText().compareTo(repeatPassword.getText()) != 0) {
            System.out.println("Passwords don't match");
            settingsErrorMessageField.setText("Passwords don't match");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setId(currentUser.getUserInfo().getId());
        userInfo.setFirstName(firstName.getText());
        userInfo.setLastName(lastName.getText());
        userInfo.setMail(mail.getText());
        userInfo.setPhone(phone.getText());
        userInfo.setAddress(address.getText());

        modifyUser.setUserInfo(userInfo);

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.MODIFY_USER, new Gson().toJson(modifyUser)));
            if (!response.isSuccess())
                settingsErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to update user");
        }
    }

    public void onLogOutPressed(ActionEvent actionEvent) {
        try {
            currentUser = null;
            Login.switchWindow((Stage) inputLogin.getScene().getWindow());
        }
        catch (IOException e) {
            System.out.println("Failed to switch window");
        }
    }
}
