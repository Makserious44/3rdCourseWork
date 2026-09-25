package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.Product;
import data.entities.RequestProduct;
import gui.misc.FactoryParam;
import gui.misc.TableCellValueFactorySetter;
import gui.misc.TablePopulator;
import gui.misc.converters.RequestProductTableEntry;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditRequestPopup {
    public Label errorMessageField;

    private static data.entities.Request requestToModify;
    public TableView<RequestProductTableEntry> productTable;
    public ComboBox<Product> selectedProduct;
    public TextField productCount;

    public static void showWindow(data.entities.Request request) throws IOException {
        Objects.requireNonNull(request, "Request must be non null");
        requestToModify = request;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(EditRequestPopup.class.getResource("/EditRequestPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Edit request");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_UNUSED_PRODUCTS, new Gson().toJson(requestToModify)));

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

        TablePopulator.populateTable(
                productTable,
                RequestType.GET_ASSOCIATED_REQUESTPRODUCTS,
                requestToModify,
                RequestProduct.class,
                RequestProductTableEntry.converter
        );
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

            var list = new ArrayList<>(productTable.getItems());
            list.add(RequestProductTableEntry.converter.convert(requestProduct));
            productTable.setItems(FXCollections.observableList(list));

            selectedProduct.getItems().remove(selectedProduct.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Incorrect number input");
        }
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        requestToModify.setProducts(productTable.getItems().stream().map(RequestProductTableEntry::getRequestProduct).collect(Collectors.toSet()));

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.MODIFY_REQUEST, new Gson().toJson(requestToModify)));

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

        var list = new ArrayList<>(productTable.getItems());
        list.remove(productTable.getSelectionModel().getSelectedItem());
        productTable.setItems(FXCollections.observableList(list));
    }
}
