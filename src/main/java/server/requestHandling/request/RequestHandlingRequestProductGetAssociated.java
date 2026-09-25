package server.requestHandling.request;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.RequestProductDAO;
import data.entities.Requestable;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingRequestProductGetAssociated implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var targetRequest = new Gson().fromJson(request.getData(), data.entities.Request.class);

        return new RequestProductDAO().getByQuery(new StoredQuery(
                "from RequestProduct where request = :request_id",
                Map.ofEntries(
                        Map.entry("request_id", targetRequest)
                )
        ));
    }
}
