package gui;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import data.entities.*;
import data.enums.UserRole;
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

public class AdminMenu {
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
    public Label mainErrorMessageField;
    public Label supplierErrorMessageField;
    public Label productErrorMessageField;
    public Label requestErrorMessageField;
    public Label orderErrorMessageField;

    @FXML
    private TableView<SupplierTableEntry> suppliersTable;

    @FXML
    private TableView<UserTableEntry> usersTable;

    @FXML
    private TableView<ProductTableEntry> productsTable;

    @FXML
    private TableView<RequestTableEntry> requestsTable;

    @FXML
    private TableView<OrderTableEntry> ordersTable;

    public static void switchWindow(Stage stage, User user) throws IOException {
        Objects.requireNonNull(user, "User cannot be null");
        currentUser = user;

        Objects.requireNonNull(user, "Received null user");
        stage.centerOnScreen();

        Parent root = FXMLLoader.load(Objects.requireNonNull(AdminMenu.class.getResource("/AdminMenu.fxml")));
        Scene newScene = new Scene(root);

        stage.setScene(newScene);

    }

    @FXML
    public void initialize() {
        new TableCellValueFactorySetter<>(usersTable).setValueFactories(List.of(
                new FactoryParam(Integer.class, "id"),
                new FactoryParam(UserRole.class, "role"),
                new FactoryParam(String.class, "firstName"),
                new FactoryParam(String.class, "lastName"),
                new FactoryParam(String.class, "phone"),
                new FactoryParam(String.class, "mail"),
                new FactoryParam(String.class, "address")
        ));

        new TableCellValueFactorySetter<>(suppliersTable).setValueFactories(List.of(
                new FactoryParam(Integer.class, "id"),
                new FactoryParam(String.class, "name"),
                new FactoryParam(String.class, "description"),
                new FactoryParam(Integer.class, "userId")
        ));

        new TableCellValueFactorySetter<>(productsTable).setValueFactories(List.of(
                new FactoryParam(Integer.class, "id"),
                new FactoryParam(String.class, "name"),
                new FactoryParam(String.class, "description"),
                new FactoryParam(Integer.class, "price")
        ));

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

        TablePopulator.populateTable(usersTable, RequestType.GET_ALL_USERS, User.class, UserTableEntry.converter);
        TablePopulator.populateTable(suppliersTable, RequestType.GET_ALL_SUPPLIERS, Supplier.class, SupplierTableEntry.converter);
        TablePopulator.populateTable(productsTable, RequestType.GET_ALL_PRODUCTS, Product.class, ProductTableEntry.converter);
        TablePopulator.populateTable(requestsTable, RequestType.GET_ALL_REQUESTS, data.entities.Request.class, RequestTableEntry.converter);
        TablePopulator.populateTable(ordersTable, RequestType.GET_ALL_ORDERS, Order.class, OrderTableEntry.converter);

        inputLogin.setText(currentUser.getLogin());
        inputPassword.setText(String.valueOf(currentUser.getPassword()));
        firstName.setText(currentUser.getUserInfo().getFirstName());
        lastName.setText(currentUser.getUserInfo().getLastName());
        phone.setText(currentUser.getUserInfo().getPhone());
        mail.setText(currentUser.getUserInfo().getMail());
        address.setText(currentUser.getUserInfo().getAddress());
    }

    // ---- USER TABLE ----

    public void onAddUserPressed(ActionEvent event) {
        try {
            CreateUserPopup.showWindow();
        }
        catch (IOException e){
            System.out.println("Failed to show window: " + e.getMessage());
        }
    }

    public void onUserRefreshPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(usersTable, RequestType.GET_ALL_USERS, User.class, UserTableEntry.converter);
    }

    public void onEditUserPressed(ActionEvent actionEvent) {
        var entry = usersTable.getSelectionModel().getSelectedItem();

        if (entry == null)
            return;

        try {
            EditUserPopup.showWindow(entry.getUser());
        }
        catch (IOException e) {
            System.out.println("Failed to display window: " + e.getMessage());
        }
    }

    public void onRemoveUserPressed(ActionEvent actionEvent) {
        UserTableEntry entry = usersTable.getSelectionModel().getSelectedItem();

        if (entry == null)
            return;

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.REMOVE_USER, new Gson().toJson(entry.getUser())));
            if (!response.isSuccess())
                mainErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to remove User");
        }
    }

    // ---- SUPPLIER TABLE ----

    public void onAddSupplierPressed(ActionEvent actionEvent) {
        try {
            CreateSupplierPopup.showWindow();
        }
        catch (IOException e){
            System.out.println("Failed to display window: " + e.getMessage());
        }
    }

    public void onSupplierRefreshPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(suppliersTable, RequestType.GET_ALL_SUPPLIERS, Supplier.class, SupplierTableEntry.converter);
    }

    public void onEditSupplierPressed(ActionEvent actionEvent) {
        SupplierTableEntry entry = suppliersTable.getSelectionModel().getSelectedItem();

        if (entry == null)
            return;

        try {
            EditSupplierPopup.showWindow(entry.getSupplier());
        }
        catch (IOException e) {
            System.out.println("Failed to display window: " + e.getMessage());
        }
    }

    public void onRemoveSupplierPressed(ActionEvent actionEvent) {
        SupplierTableEntry entry = suppliersTable.getSelectionModel().getSelectedItem();

        if (entry == null) {
            return;
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.REMOVE_SUPPLIER, new Gson().toJson(entry.getSupplier())));
            if (!response.isSuccess())
                supplierErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to remove Supplier");
        }
    }

    // ---- PRODUCT TABLE ----

    public void onAddProductPressed(ActionEvent actionEvent) {
        try {
            CreateProductPopup.showWindow();
        }
        catch (IOException e) {
            System.out.println("Failed to display window: " + e.getMessage());
        }
    }

    public void onEditProductPressed(ActionEvent actionEvent) {
        ProductTableEntry entry = productsTable.getSelectionModel().getSelectedItem();

        if (entry == null)
            return;

        try {
            EditProductPopup.showWindow(entry.getProduct());
        }
        catch (IOException e) {
            System.out.println("Failed to display window: " + e.getMessage());
        }
    }

    public void onRemoveProductPressed(ActionEvent actionEvent) {
        ProductTableEntry entry = productsTable.getSelectionModel().getSelectedItem();

        if (entry == null) {
            return;
        }

        try {
            var response = RequestSequence.sendRequest(new Request(RequestType.REMOVE_PRODUCT, new Gson().toJson(entry.getProduct())));
            if (!response.isSuccess())
                supplierErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to remove Product");
        }
    }

    public void onProductRefreshedPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(productsTable, RequestType.GET_ALL_PRODUCTS, Product.class, ProductTableEntry.converter);
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
        TablePopulator.populateTable(requestsTable, RequestType.GET_ALL_REQUESTS, data.entities.Request.class, RequestTableEntry.converter);
    }

    // ---- Order ----

    public void onAddOrderPressed(ActionEvent actionEvent) {
        try {
            CreateOrderPopup.showWindow();
        }
        catch (IOException e) {
            System.out.println("Couldn't show window");
        }
    }

    public void onEditOrderPressed(ActionEvent actionEvent) {
        try {
            var entry = ordersTable.getSelectionModel().getSelectedItem().getOrder();
            if (entry == null)
                return;
            EditOrderPopup.showWindow(entry);
        }
        catch (IOException e) {
            System.out.println("Couldn't show window    ");
        }
    }

    public void onRemoveOrderPressed(ActionEvent actionEvent) {
        OrderTableEntry entry = ordersTable.getSelectionModel().getSelectedItem();

        try {
            var response = RequestSequence.sendRequest(
                    new Request(RequestType.REMOVE_ORDER, new Gson().toJson(entry.getOrder()))
            );
            if (!response.isSuccess())
                requestErrorMessageField.setText(response.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failed to remove Request");
        }
    }

    public void onOrderRefreshedPressed(ActionEvent actionEvent) {
        TablePopulator.populateTable(ordersTable, RequestType.GET_ALL_ORDERS, Order.class, OrderTableEntry.converter);
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
