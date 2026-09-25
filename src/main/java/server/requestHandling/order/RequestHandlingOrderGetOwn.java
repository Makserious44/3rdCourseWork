package server.requestHandling.order;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.OrderDAO;
import data.DAO.RequestDAO;
import data.entities.Requestable;
import data.entities.User;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingOrderGetOwn implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var owningUser = new Gson().fromJson(request.getData(), User.class);

        return new OrderDAO().getByQuery(new StoredQuery(
                "from Order where orderer.id = :orderer",
                Map.of("orderer", owningUser.getId())
        ));
    }
}
