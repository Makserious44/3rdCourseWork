package gui.misc;

import client.RequestSequence;
import com.google.gson.Gson;
import connection.Request;
import connection.RequestType;
import gui.misc.converters.ITableConverter;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TablePopulator {
    private TablePopulator() {}

    public static <T, M> void populateTable(TableView<M> table, RequestType requestType, Class<T> serverResponseType, ITableConverter<M, T> converter) {
        try {
            var response = RequestSequence.sendRequest(new Request(requestType, null));

            List<T> responseObjects = RequestSequence.deserializeResponse(response, serverResponseType);

            if (responseObjects == null) {
                table.setItems(FXCollections.observableArrayList());
                return;
            }

            List<M> tableEntries = responseObjects.stream().map(converter::convert).toList();

//            System.out.println(tableEntries);

            table.setItems(FXCollections.observableList(tableEntries));
//            usersTable.setItems(FXCollections.observableList(users));
        }
        catch (IOException e) {
            System.out.println("Failed to fetch users from server: " + e.getMessage());
        }
    }

    public static <T, M> void populateTable(TableView<M> table, RequestType requestType, Object requestData, Class<T> serverResponseType, ITableConverter<M, T> converter) {
        try {
            var response = RequestSequence.sendRequest(new Request(requestType, new Gson().toJson(requestData)));

            List<T> responseObjects = RequestSequence.deserializeResponse(response, serverResponseType);

            if (responseObjects == null) {
                table.setItems(FXCollections.observableArrayList());
                return;
            }

            List<M> tableEntries = responseObjects.stream().map(converter::convert).toList();

//            System.out.println(tableEntries);

            table.setItems(FXCollections.observableList(tableEntries));
//            usersTable.setItems(FXCollections.observableList(users));
        }
        catch (IOException e) {
            System.out.println("Failed to fetch users from server: " + e.getMessage());
        }
    }
}
