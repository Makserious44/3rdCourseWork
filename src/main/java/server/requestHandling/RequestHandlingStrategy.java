package server.requestHandling;

import connection.Request;
import data.DAO.DAOException;
import data.entities.Requestable;

import java.util.List;

public interface RequestHandlingStrategy {
    List<? extends Requestable> action(Request request) throws DAOException;
}
