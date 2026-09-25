package server.requestHandling.order;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.OrderProductDAO;
import data.DAO.RequestProductDAO;
import data.entities.Order;
import data.entities.OrderProduct;
import data.entities.Requestable;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingOrderProductGetAssociated implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var targetOrder = new Gson().fromJson(request.getData(), Order.class);

        return new OrderProductDAO().getByQuery(new StoredQuery(
                "from OrderProduct where order = :order_id",
                Map.ofEntries(
                        Map.entry("order_id", targetOrder)
                )
        ));
    }
}
