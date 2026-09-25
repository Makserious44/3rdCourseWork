package server.requestHandling.order;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.OrderDAO;
import data.DAO.RequestDAO;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;

public class RequestHandlingOrderGetAll implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        return new OrderDAO().getAllEntries();
    }
}
