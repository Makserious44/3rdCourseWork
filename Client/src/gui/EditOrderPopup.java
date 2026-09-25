package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.RequestType;
import data.entities.*;
import gui.misc.FactoryParam;
import gui.misc.TableCellValueFactorySetter;
import gui.misc.converters.OrderProductTableEntry;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditOrderPopup {
    public ComboBox<Supplier> selectedSupplier;
    public TableView<OrderProductTableEntry> productTable;
    public ComboBox<Product> selectedProduct;
    public TextField productCount;
    public Label errorMessageField;

    private static Order orderToModify;

    public static void showWindow(Order order) throws IOException {
        Objects.requireNonNull(order);
        orderToModify = order;

        Stage primaryStage = new Stage();
        Parent rootScene = FXMLLoader.load(Objects.requireNonNull(CreateUserPopup.class.getResource("/EditOrderPopup.fxml")));

        primaryStage.centerOnScreen();

        primaryStage.setTitle("Edit order");
        primaryStage.setScene(new Scene(rootScene));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        try {
            var response = RequestSequence.sendRequest(
                    new connection.Request(RequestType.GET_UNORDERED_PRODUCTS,
                    new Gson().toJson(orderToModify))
            );

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
            var response = RequestSequence.sendRequest(new connection.Request(RequestType.GET_ALL_SUPPLIERS, null));

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

        new TableCellValueFactorySetter<>(productTable).setValueFactories(List.of(
                new FactoryParam(String.class, "productName"),
                new FactoryParam(Integer.class, "productCount")
        ));

        selectedSupplier.setValue(orderToModify.getSupplier());
        productTable.setItems(FXCollections.observableList(
                orderToModify.getProducts().stream().map(OrderProductTableEntry::new).toList()
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

            var list = new ArrayList<>(productTable.getItems());
            list.add(OrderProductTableEntry.converter.convert(orderProduct));
            productTable.setItems(FXCollections.observableList(list));

            selectedProduct.getItems().remove(selectedProduct.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            errorMessageField.setText("Incorrect number input");
        }
    }

    public void onConfirmPressed(ActionEvent actionEvent) {
        if (selectedSupplier == null || productTable.getItems().isEmpty()) {
            errorMessageField.setText("All fields must be filled");
        }

        orderToModify.setProducts(productTable.getItems().stream().map(OrderProductTableEntry::getOrderProduct).collect(Collectors.toSet()));
        orderToModify.getProducts().forEach(rp -> rp.setOrder(orderToModify));
        orderToModify.setSupplier(selectedSupplier.getValue());

        try {
            var response = RequestSequence.sendRequest(
                    new connection.Request(RequestType.MODIFY_ORDER,
                            new Gson().toJson(orderToModify))
            );

            if (!response.isSuccess())
                errorMessageField.setText(response.getMessage());
            else {
                ((Stage) errorMessageField.getScene().getWindow()).close();
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't modify Order");
            errorMessageField.setText("Failed to modify Order");
        }
    }

    public void onRemovePressed(ActionEvent actionEvent) {
        selectedProduct.getItems().add(productTable.getSelectionModel().getSelectedItem().getOrderProduct().getProduct());

        var list = new ArrayList<>(productTable.getItems());
        list.remove(productTable.getSelectionModel().getSelectedItem());
        productTable.setItems(FXCollections.observableList(list));
    }
}
