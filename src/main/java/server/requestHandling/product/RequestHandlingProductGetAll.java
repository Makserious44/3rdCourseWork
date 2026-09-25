package server.requestHandling.product;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.ProductDAO;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;

public class RequestHandlingProductGetAll implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        return new ProductDAO().getAllEntries();
    }
}
