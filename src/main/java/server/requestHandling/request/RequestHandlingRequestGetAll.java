package server.requestHandling.request;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.RequestDAO;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;

public class RequestHandlingRequestGetAll implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        return new RequestDAO().getAllEntries();
    }
}
