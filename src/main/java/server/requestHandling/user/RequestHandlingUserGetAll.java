package server.requestHandling.user;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.UserDAO;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;

public class RequestHandlingUserGetAll implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        return new UserDAO().getAllEntries();
    }
}
