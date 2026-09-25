package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Supplier;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import data.entities.User;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.Objects;

public class CreateSupplierPopup {
    public ComboBox<User> selectedUser;
    public TextField inputName;
    public TextArea inputDescription;
    public Label errorMessageField;

    public static void showWindow() throws IOException {
        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/CreateSupplierPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Create supplier");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_UNREGISTERED_PARTNER_USERS, null));
            if (!response.isSuccess()) {
                errorMessageField.setText("No unregistered suppliers found");
                return;
            }

            List<User> partners = RequestSequence.deserializeResponse(response, User.class);
            selectedUser.setItems(FXCollections.observableList(partners));
        }
        catch (IOException e) {
            System.out.println("Couldn't get partners: " + e.getMessage());
            errorMessageField.setText("Failed to request partners");
        }
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) inputName.getScene().getWindow()).close();
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        Supplier supplier = new Supplier();

        if (
                selectedUser.getSelectionModel().isEmpty() ||
                inputName.getText().isEmpty() ||
                inputDescription.getText().isEmpty()) {

            System.out.println("All fields must be filled");
            errorMessageField.setText("All fields must be filled");
            return;
        }

        supplier.setName(inputName.getText());
        supplier.setDescription(inputDescription.getText());
        supplier.setUser(selectedUser.getSelectionModel().getSelectedItem());

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.ADD_SUPPLIER, new Gson().toJson(supplier)));
            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) inputName.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Failed to add Supplier: " + e.getMessage());
            errorMessageField.setText("Failed to add Supplier");
        }
    }
}
