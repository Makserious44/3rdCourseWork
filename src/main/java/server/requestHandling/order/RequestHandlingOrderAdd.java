package server.requestHandling.order;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.OrderDAO;
import data.DAO.RequestDAO;
import data.entities.Order;
import data.entities.RequestProduct;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingOrderAdd implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var orderToAdd = new Gson().fromJson(request.getData(), Order.class);

        for (var op : orderToAdd.getProducts()) {
            op.setOrder(orderToAdd);
//            new RequestProductDAO().addEntry(rp);
        }

        System.out.println("----");
        System.out.println(orderToAdd);
        System.out.println("++++");

        new OrderDAO().addEntry(orderToAdd);
        return Collections.singletonList(orderToAdd);
    }
}
