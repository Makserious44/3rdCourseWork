package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.*;
import gui.misc.FactoryParam;
import gui.misc.TableCellValueFactorySetter;
import gui.misc.converters.OrderProductTableEntry;
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

public class CreateOrderPopup {
    public ComboBox<data.entities.Request> selectedRequest;
    public ComboBox<User> selectedOrderer;
    public ComboBox<Supplier> selectedSupplier;
    public TableView<OrderProductTableEntry> productTable;
    public ComboBox<Product> selectedProduct;
    public TextField productCount;
    public Label errorMessageField;

    boolean requestIsUsed = false;

    public static void showWindow() throws IOException {
        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/CreateOrderPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Create order");
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

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_ALL_USERS, null));

            if (!response.isSuccess()) {
                errorMessageField.setText("No users found");
            }
            else {
                selectedOrderer.setItems(FXCollections.observableList(
                        RequestSequence.deserializeResponse(response, User.class))
                );
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't fetch Users");
            errorMessageField.setText("Couldn't fetch users");
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_ALL_SUPPLIERS, null));

            if (!response.isSuccess()) {
                errorMessageField.setText("No suppliers found");
            }
            else {
                selectedSupplier.setItems(FXCollections.observableList(
                        RequestSequence.deserializeResponse(response, Supplier.class))
                );
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't fetch Suppliers");
            errorMessageField.setText("Couldn't fetch suppliers");
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.GET_ALL_REQUESTS, null));

            if (!response.isSuccess()) {
                errorMessageField.setText("No requests found");
            }
            else {
                selectedRequest.setItems(FXCollections.observableList(
                        RequestSequence.deserializeResponse(response, data.entities.Request.class))
                );
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't fetch Requests");
            errorMessageField.setText("Couldn't fetch requests");
        }

        new TableCellValueFactorySetter<>(productTable).setValueFactories(List.of(
                new FactoryParam(String.class, "productName"),
                new FactoryParam(Integer.class, "productCount")
        ));
    }

    public void onCancelPressed(ActionEvent actionEvent) {
        ((Stage) errorMessageField.getScene().getWindow()).close();
    }

    public void onAddProductPressed(ActionEvent actionEvent) {
        if (selectedProduct.getSelectionModel().isEmpty()) {
            errorMessageField.setText("No product selected");
            return;
        }

        if (productCount.getText().isEmpty()) {
            errorMessageField.setText("Input product quantity");
            return;
        }

        try {
            var orderProduct = new OrderProduct();
            orderProduct.setProduct(selectedProduct.getSelectionModel().getSelectedItem());

            var count = Integer.parseInt(productCount.getText());
            if (count <= 0)
                throw new NumberFormatException("Number can't be less than 1");
            orderProduct.setProductCount(count);

            productTable.getItems().add(OrderProductTableEntry.converter.convert(orderProduct));
            selectedProduct.getItems().remove(selectedProduct.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Incorrect number input");
        }
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (selectedOrderer.getValue() == null || selectedSupplier == null || productTable.getItems().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
        }

        var order = new Order();
        order.setProducts(productTable.getItems().stream().map(OrderProductTableEntry::getOrderProduct).collect(Collectors.toSet()));
        order.getProducts().forEach(rp -> rp.setOrder(order));
        order.setOrderer(selectedOrderer.getValue());
        order.setSupplier(selectedSupplier.getValue());

        try {
            var response = RequestSequence.sendRequest(
                    new Request(RequestType.ADD_ORDER,
                            new Gson().toJson(order))
            );

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else {
                if (requestIsUsed) {
                    try {
                        RequestSequence.sendRequest(
                                new Request(RequestType.REMOVE_REQUEST, new Gson().toJson(selectedRequest.getValue()))
                        );
                    }
                    catch (IOException ignored) { }
                }
                ((Stage) errorMessageField.getScene().getWindow()).close();
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't modify Request");
            errorMessageField.setText("Failed to modify Request");
        }
    }

    public void onRemovePressed(ActionEvent actionEvent) {
        selectedProduct.getItems().add(productTable.getSelectionModel().getSelectedItem().getOrderProduct().getProduct());
        productTable.getItems().remove(productTable.getSelectionModel().getSelectedItem());
    }

    public void onResetRequestPressed(ActionEvent actionEvent) {
        requestIsUsed = false;

        selectedOrderer.setValue(null);
        selectedOrderer.setDisable(false);

        selectedRequest.setValue(null);
        selectedRequest.setDisable(false);

        selectedProduct.setDisable(false);
        productCount.setDisable(false);

        productTable.setItems(FXCollections.observableArrayList());
        productTable.setDisable(false);
    }

    public void onUseRequestPressed(ActionEvent actionEvent) {
        if (selectedRequest.getValue() == null)
            return;

        var request = selectedRequest.getValue();

        System.out.println(request);
        requestIsUsed = true;

        selectedOrderer.setValue(request.getSubmitter());
        selectedOrderer.setDisable(true);

        selectedRequest.setDisable(true);
        selectedProduct.setDisable(true);
        productCount.setDisable(true);

        System.out.println(request);
        productTable.setItems(FXCollections.observableList(
                request.getProducts().stream().map(OrderProductTableEntry::new).toList()
        ));
        productTable.setDisable(true);


    }
}
