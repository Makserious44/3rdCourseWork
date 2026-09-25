package server.requestHandling.user;

import connection.Request;
import data.DAO.DAOException;
import data.DAO.UserDAO;
import data.entities.Requestable;
import data.enums.UserRole;
import data.misc.StoredQuery;
import server.requestHandling.RequestHandlingStrategy;

import java.util.List;
import java.util.Map;

public class RequestHandlingUserGetPartners implements RequestHandlingStrategy {
    @Override
    public List<? extends Requestable> action(Request request) throws DAOException {
        StoredQuery query = new StoredQuery("from User where role = :role and id not in (select user.id from Supplier)", Map.ofEntries(Map.entry("role", UserRole.partner)));
        return new UserDAO().getByQuery(query);
    }
}
