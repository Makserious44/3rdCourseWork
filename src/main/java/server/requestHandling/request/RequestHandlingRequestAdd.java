package server.requestHandling.request;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.RequestDAO;
import data.DAO.RequestProductDAO;
import data.entities.RequestProduct;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingRequestAdd implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        var requestToAdd = new Gson().fromJson(request.getData(), data.entities.Request.class);

        for (RequestProduct rp : requestToAdd.getProducts()) {
            rp.setRequest(requestToAdd);
//            new RequestProductDAO().addEntry(rp);
        }

        System.out.println("----");
        System.out.println(requestToAdd);
        System.out.println("++++");

        new RequestDAO().addEntry(requestToAdd);
        return Collections.singletonList(requestToAdd);
    }
}
