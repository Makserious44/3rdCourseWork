package server.requestHandling.user;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.SupplierDAO;
import data.DAO.UserDAO;
import data.entities.Requestable;
import data.entities.User;
import data.enums.UserRole;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RequestHandlingUserModify implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        User modifyUser = new Gson().fromJson(request.getData(), User.class);

        if (new UserDAO().findEntry(modifyUser.getId()).getRole() == UserRole.partner &&
            modifyUser.getRole() != UserRole.partner) {
            try {
                new SupplierDAO().removeEntry(new SupplierDAO().getByQuery
                        (new StoredQuery("from Supplier s where s.user = :user",
                                Map.of("user", modifyUser))).get(0));
            }
            catch (IndexOutOfBoundsException ignored) {}
        }

        new UserDAO().overwriteEntry(modifyUser);
        return Collections.singletonList(modifyUser);
    }
}
