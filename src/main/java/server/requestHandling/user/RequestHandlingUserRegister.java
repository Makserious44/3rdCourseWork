package server.requestHandling.user;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.UserDAO;
import data.entities.Requestable;
import data.entities.User;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RequestHandlingUserRegister implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) {
        User registerUser = new Gson().fromJson(request.getData(), User.class);
        System.out.println("!!!!" + registerUser);
//        UserInfo userInfo = registerUser.getUserInfo();
//        new UserInfoDAO().addEntry(userInfo);
//        registerUser.setUserInfo(userInfo);
//        registerUser.setRole(UserRole.client);
//        new UserInfoDAO().addEntry(registerUser.getUserInfo());
        if (new UserDAO().getByQuery(new StoredQuery(
                "from User where login = :login",
                Map.of("login", registerUser.getLogin()))
        ).isEmpty()) {

            new UserDAO().addEntry(registerUser);
            return Collections.singletonList(registerUser);
        }
        else {
            return null;
        }
    }
}
