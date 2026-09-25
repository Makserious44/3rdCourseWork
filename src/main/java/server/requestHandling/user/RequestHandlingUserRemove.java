package server.requestHandling.user;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.UserDAO;
import data.entities.Requestable;
import data.entities.User;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingUserRemove implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        User userToRemove = new Gson().fromJson(request.getData(), User.class);
        new UserDAO().removeEntry(userToRemove);
        return Collections.singletonList(userToRemove);
    }
}
