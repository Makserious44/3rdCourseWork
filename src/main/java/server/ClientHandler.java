package server;

import com.google.gson.Gson;
import connection.Request;
import connection.Response;
import server.requestHandling.*;
import server.requestHandling.order.*;
import server.requestHandling.product.*;
import server.requestHandling.request.*;
import server.requestHandling.supplier.RequestHandlingSupplierAdd;
import server.requestHandling.supplier.RequestHandlingSupplierGetAll;
import server.requestHandling.supplier.RequestHandlingSupplierModify;
import server.requestHandling.supplier.RequestHandlingSupplierRemove;
import server.requestHandling.user.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final RequestHandler requestHandler;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        requestHandler = new RequestHandler();

        System.out.println("Client handling initialized");
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            Request request = null;
            try {
                String clientRequest = in.readLine();
//                System.out.println(clientRequest);
                request = new Gson().fromJson(clientRequest, Request.class);
            }
            catch (IOException e) {
                System.out.println("Client disconnected unexpectedly");
                try {
                    socket.close();
                }
                catch (IOException ignored) {}
                return;
            }

            if (request == null) {
                System.out.println("Couldn't parse request");
                out.println(new Gson().toJson(new Response(false, "Failed to read request", null)));
                continue;
            }

            System.out.println(request);

            respondToUserRequest(request);
        }
    }

    private void respondToUserRequest(Request request) {
        System.out.println(request.getRequestType());
        switch (request.getRequestType()) {
            case LOGIN -> {
                requestHandler.setStrategy(new RequestHandlingUserLogin());
            }
            case REGISTER -> {
                requestHandler.setStrategy(new RequestHandlingUserRegister());
            }
            case GET_ALL_USERS -> {
                requestHandler.setStrategy(new RequestHandlingUserGetAll());
            }
            case MODIFY_USER -> {
                requestHandler.setStrategy(new RequestHandlingUserModify());
            }
            case REMOVE_USER -> {
                requestHandler.setStrategy(new RequestHandlingUserRemove());
            }
            case GET_UNREGISTERED_PARTNER_USERS -> {
                requestHandler.setStrategy(new RequestHandlingUserGetPartners());
            }
            case ADD_SUPPLIER -> {
                requestHandler.setStrategy(new RequestHandlingSupplierAdd());
            }
            case GET_ALL_SUPPLIERS -> {
                requestHandler.setStrategy(new RequestHandlingSupplierGetAll());
            }
            case MODIFY_SUPPLIER -> {
                requestHandler.setStrategy(new RequestHandlingSupplierModify());
            }
            case REMOVE_SUPPLIER -> {
                requestHandler.setStrategy(new RequestHandlingSupplierRemove());
            }
            case GET_ALL_PRODUCTS -> {
                requestHandler.setStrategy(new RequestHandlingProductGetAll());
            }
            case ADD_PRODUCT -> {
                requestHandler.setStrategy(new RequestHandlingProductAdd());
            }
            case MODIFY_PRODUCT -> {
                requestHandler.setStrategy(new RequestHandlingProductModify());
            }
            case REMOVE_PRODUCT -> {
                requestHandler.setStrategy(new RequestHandlingProductRemove());
            }
            case GET_UNUSED_PRODUCTS -> {
                requestHandler.setStrategy(new RequestHandlingProductGetUnused());
            }
            case GET_UNORDERED_PRODUCTS -> {
                requestHandler.setStrategy(new RequestHandlingProductGetUnordered());
            }
            case GET_ALL_REQUESTS -> {
                requestHandler.setStrategy(new RequestHandlingRequestGetAll());
            }
            case ADD_REQUEST -> {
                requestHandler.setStrategy(new RequestHandlingRequestAdd());
            }
            case MODIFY_REQUEST -> {
                requestHandler.setStrategy(new RequestHandlingRequestModify());
            }
            case REMOVE_REQUEST -> {
                requestHandler.setStrategy(new RequestHandlingRequestRemove());
            }
            case GET_OWN_REQUESTS -> {
                requestHandler.setStrategy(new RequestHandlingRequestGetOwn());
            }
            case GET_ASSOCIATED_REQUESTPRODUCTS -> {
                requestHandler.setStrategy(new RequestHandlingRequestProductGetAssociated());
            }
            case GET_ALL_ORDERS -> {
                requestHandler.setStrategy(new RequestHandlingOrderGetAll());
            }
            case ADD_ORDER -> {
                requestHandler.setStrategy(new RequestHandlingOrderAdd());
            }
            case MODIFY_ORDER -> {
                requestHandler.setStrategy(new RequestHandlingOrderModify());
            }
            case REMOVE_ORDER -> {
                requestHandler.setStrategy(new RequestHandlingOrderRemove());
            }
            case GET_OWN_ORDERS -> {
                requestHandler.setStrategy(new RequestHandlingOrderGetOwn());
            }
            case GET_ASSOCIATED_ORDERS -> {
                requestHandler.setStrategy(new RequestHandlingOrderGetAssociated());
            }
            case GET_ASSOCIATED_ORDERPRODUCTS -> {
                requestHandler.setStrategy(new RequestHandlingOrderProductGetAssociated());
            }
            default -> {
                System.out.println("BRUHHHH");
                out.println(new Gson().toJson(new Response(false, "Bruh", null)));
            }
        }

        out.println(new Gson().toJson(requestHandler.handleRequest(request)));
        System.out.println();
    }
}
