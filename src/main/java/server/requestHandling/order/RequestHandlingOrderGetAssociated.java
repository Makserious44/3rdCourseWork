package server.requestHandling.order;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.OrderDAO;
import data.entities.Requestable;
import data.entities.User;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingOrderGetAssociated implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var associatedUser = new Gson().fromJson(request.getData(), User.class);

        return new OrderDAO().getByQuery(new StoredQuery(
                "from Order where orderer.id = :user or supplier.user.id = :user",
                Map.of("user", associatedUser.getId())
        ));
    }
}
