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

public class RequestHandlingOrderModify implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var orderToModify = new Gson().fromJson(request.getData(), Order.class);

        for (var op : orderToModify.getProducts()) {
            op.setOrder(orderToModify);
//            new RequestProductDAO().addEntry(rp);
        }

        new OrderDAO().overwriteEntry(orderToModify);
        return Collections.singletonList(orderToModify);
    }
}
