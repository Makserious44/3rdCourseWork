package server.requestHandling.supplier;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.SupplierDAO;
import data.entities.Requestable;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;

public class RequestHandlingSupplierGetAll implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        return new SupplierDAO().getAllEntries();
    }
}
