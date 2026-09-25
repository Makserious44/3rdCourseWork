package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Product;
import data.entities.RequestProduct;
import data.entities.User;
import gui.misc.FactoryParam;
import gui.misc.TableCellValueFactorySetter;
import gui.misc.converters.RequestProductTableEntry;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateRequestPopup {
    public Label errorMessageField;
    public TableView<RequestProductTableEntry> productTable;
    public ComboBox<Product> selectedProduct;
    public TextField productCount;

    private static User currentUser;

    public static void showWindow(User user) throws IOException {
        Objects.requireNonNull(user);
        currentUser = user;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/CreateRequestPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Create request");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_ALL_PRODUCTS, null));

            if (!response.isSuccess()) {
                errorMessageField.setText("No products found");
            }
            else {
                selectedProduct.setItems(FXCollections.observableList(
                        RequestSequence.deserializeResponse(response, Product.class))
                );
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't fetch Products");
            errorMessageField.setText("Couldn't fetch products");
        }

        new TableCellValueFactorySetter<>(productTable).setValueFactories(List.of(
                new FactoryParam(String.class, "productName"),
                new FactoryParam(Integer.class, "productCount")
        ));
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) errorMessageField.getScene().getWindow()).close();
    }

    public void OnAddProductPressed(ActionEvent actionEvent) {
        if (selectedProduct.getSelectionModel().isEmpty()) {
            errorMessageField.setText("No product selected");
            return;
        }

        if (productCount.getText().isEmpty()) {
            errorMessageField.setText("Input product quantity");
            return;
        }

        try {
            var requestProduct = new RequestProduct();
            requestProduct.setProduct(selectedProduct.getSelectionModel().getSelectedItem());

            var count = Integer.parseInt(productCount.getText());
            if (count <= 0)
                throw new NumberFormatException("Number can't be less than 1");
            requestProduct.setProductCount(count);

            productTable.getItems().add(RequestProductTableEntry.converter.convert(requestProduct));
            selectedProduct.getItems().remove(selectedProduct.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Incorrect number input");
        }
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (productTable.getItems().isEmpty()) {
            return;
        }

        var request = new data.entities.Request();
        request.setProducts(productTable.getItems().stream().map(RequestProductTableEntry::getRequestProduct).collect(Collectors.toSet()));
        request.getProducts().forEach(rp -> rp.setRequest(request));
        request.setSubmitter(currentUser);

        try {
            var response = RequestSequence.sendRequest(
                    new Request(RequestType.ADD_REQUEST,
                    new Gson().toJson(request))
            );

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else
                ((Stage) errorMessageField.getScene().getWindow()).close();
        }
        catch (IOException e) {
            System.out.println("Couldn't modify Request");
            errorMessageField.setText("Failed to modify Request");
        }
    }

    public void onRemovePressed(ActionEvent actionEvent) {
        selectedProduct.getItems().add(productTable.getSelectionModel().getSelectedItem().getRequestProduct().getProduct());
        productTable.getItems().remove(productTable.getSelectionModel().getSelectedItem());
    }
}
