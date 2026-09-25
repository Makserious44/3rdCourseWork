package data.DAO;

import data.entities.Request;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RequestDAO implements DAO<Request> {
    @Override
    public void addEntry(Request entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to add Request: " + e.getMessage());
        }
    }

    @Override
    public void overwriteEntry(Request newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to update Request");
        }
    }

    @Override
    public void removeEntry(Request entry) {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to remove Request");
        }
    }

    @Override
    public Request findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(Request.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Request");
        }
    }

    @Override
    public List<Request> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), Request.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Request");
        }
    }

    @Override
    public List<Request> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from Request", Request.class).getResultList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to find Requests");
        }
    }
}
