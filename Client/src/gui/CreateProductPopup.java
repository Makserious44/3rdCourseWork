package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CreateProductPopup {
    public TextField inputName;
    public TextArea inputDescription;
    public TextField inputPrice;
    public Label errorMessageField;

    public static void showWindow() throws IOException {
        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/CreateProductPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Create product");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) inputName.getScene().getWindow()).close();
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        Product product = new Product();
        if (inputName.getText().isEmpty() || inputDescription.getText().isEmpty() || inputPrice.getText().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
            return;
        }

        try {
            product.setName(inputName.getText());
            product.setDescription(inputDescription.getText());
            product.setRetailPrice(Integer.parseInt(inputPrice.getText()));
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Price must be a number");
            return;
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.ADD_PRODUCT, new Gson().toJson(product)));

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) inputName.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Couldn't modify Product");
            errorMessageField.setText("Failed to modify Product");
        }
    }
}
