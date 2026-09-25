package server.requestHandling.product;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.ProductDAO;
import data.DAO.RequestDAO;
import data.DAO.RequestProductDAO;
import data.entities.RequestProduct;
import data.entities.Requestable;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingProductGetUnused implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var targetRequest = new Gson().fromJson(request.getData(), data.entities.Request.class);

        return new ProductDAO().getByQuery(new StoredQuery(
                "from Product where id not in (select product.id from RequestProduct where request = :id)",
                Map.ofEntries(
                        Map.entry("id", targetRequest)
                )
        ));
    }
}
