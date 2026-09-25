package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Product;
import data.entities.RequestProduct;
import gui.misc.converters.RequestProductTableEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditProductPopup {
    private static Product productToModify;

    public Label errorMessageField;
    public TextField inputName;
    public TextArea inputDescription;
    public TextField inputPrice;

    public static void showWindow(Product product) throws IOException {
        Objects.requireNonNull(product, "Product must be non null");
        productToModify = product;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(EditProductPopup.class.getResource("/EditProductPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Edit product");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        inputName.setText(productToModify.getName());
        inputDescription.setText(productToModify.getDescription());
        inputPrice.setText(String.valueOf(productToModify.getRetailPrice()));
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) errorMessageField.getScene().getWindow()).close();
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (inputName.getText().isEmpty() || inputDescription.getText().isEmpty() || inputPrice.getText().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            productToModify.setName(inputName.getText());
            productToModify.setDescription(inputDescription.getText());
            productToModify.setRetailPrice(Integer.parseInt(inputPrice.getText()));
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Incorrect input");
            return;
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.MODIFY_PRODUCT, new Gson().toJson(productToModify)));

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) errorMessageField.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Couldn't modify Product");
            errorMessageField.setText("Failed to modify Product");
        }
    }
}