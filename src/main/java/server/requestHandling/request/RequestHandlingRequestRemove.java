package server.requestHandling.request;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.RequestDAO;
import data.entities.RequestProduct;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingRequestRemove implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var requestToRemove = new Gson().fromJson(request.getData(), data.entities.Request.class);

        for (RequestProduct rp : requestToRemove.getProducts()) {
            rp.setRequest(requestToRemove);
//            new RequestProductDAO().addEntry(rp);
        }

        new RequestDAO().removeEntry(requestToRemove);
        return Collections.singletonList(requestToRemove);
    }
}
