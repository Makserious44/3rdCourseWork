package server.requestHandling.supplier;

import com.google.gson.Gson;
import connection.Request;
import data.DAO.DAOException;
import data.DAO.SupplierDAO;
import data.entities.Requestable;
import data.entities.Supplier;
import server.requestHandling.RequestHandlingStrategy;

import java.util.Collections;
import java.util.List;

public class RequestHandlingSupplierRemove implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        Supplier supplier = new Gson().fromJson(request.getData(), Supplier.class);
        new SupplierDAO().removeEntry(supplier);
        return Collections.singletonList(supplier);
    }
}
