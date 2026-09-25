package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Supplier;
import data.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditSupplierPopup {
    public TextField inputName;
    public TextArea inputDescription;
    public Label errorMessageField;

    private static Supplier supplierToModify;

    public static void showWindow(Supplier supplier) throws IOException {
        Objects.requireNonNull(supplier, "Supplier must be non null");
        supplierToModify = supplier;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/EditSupplierPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Edit supplier");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        inputName.setText(supplierToModify.getName());
        inputDescription.setText(supplierToModify.getDescription());
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) inputName.getScene().getWindow()).close();
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (inputName.getText().isEmpty() || inputDescription.getText().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
            return;
        }

        supplierToModify.setName(inputName.getText());
        supplierToModify.setDescription(inputDescription.getText());

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.MODIFY_SUPPLIER, new Gson().toJson(supplierToModify)));

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) inputName.getScene().getWindow()).close();
        } catch (IOException e) {
            System.out.println("Couldn't modify supplier: " + e.getMessage());
            errorMessageField.setText("Failed to modify supplier");
        }
    }
}
