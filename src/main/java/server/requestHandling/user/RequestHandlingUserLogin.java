package server.requestHandling.user;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.UserDAO;
import data.entities.Requestable;
import data.entities.User;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingUserLogin implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        User loginUser;

        loginUser = new Gson().fromJson(request.getData(), User.class);

        if (loginUser == null)
            return null;

//        User finalLoginUser = loginUser;
        return new UserDAO().getByQuery(
                new StoredQuery(
                        "from User where login = :login and password = :password",
                        Map.ofEntries(
                                Map.entry("login", loginUser.getLogin()),
                                Map.entry("password", loginUser.getPassword()))
                )
        );
//        return new UserDAO()
//                .getAllEntries()
//                .stream()
//                .filter((User a) ->
//                    a.getLogin().compareToIgnoreCase(finalLoginUser.getLogin()) == 0 &&
//                    a.getPassword() == finalLoginUser.getPassword())
//                .toList();
    }
}
