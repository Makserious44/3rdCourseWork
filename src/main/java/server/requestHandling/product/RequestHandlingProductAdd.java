package server.requestHandling.product;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.ProductDAO;
import data.entities.Product;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingProductAdd implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        Product product = new Gson().fromJson(request.getData(), Product.class);
        new ProductDAO().addEntry(product);
        return Collections.singletonList(product);
    }
}
