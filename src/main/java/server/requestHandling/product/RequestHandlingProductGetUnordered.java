package server.requestHandling.product;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.ProductDAO;
import data.entities.Order;
import data.entities.Requestable;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingProductGetUnordered implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var targetOrder = new Gson().fromJson(request.getData(), Order.class);

        return new ProductDAO().getByQuery(new StoredQuery(
                "from Product where id not in (select product.id from OrderProduct where order = :id)",
                Map.ofEntries(
                        Map.entry("id", targetOrder)
                )
        ));
    }
}
