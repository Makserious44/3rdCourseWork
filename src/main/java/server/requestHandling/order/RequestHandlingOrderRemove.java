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

public class RequestHandlingOrderRemove implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var orderToRemove = new Gson().fromJson(request.getData(), Order.class);

        for (var op : orderToRemove.getProducts()) {
            op.setOrder(orderToRemove);
//            new RequestProductDAO().addEntry(rp);
        }

        new OrderDAO().removeEntry(orderToRemove);
        return Collections.singletonList(orderToRemove);
    }
}
