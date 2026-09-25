package data.DAO;

import data.entities.*;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderDAO implements DAO<Order> {
    @Override
    public void addEntry(Order entry) throws DAOException {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to add Order: " + e.getMessage());
        }
    }

    @Override
    public void overwriteEntry(Order newEntry) throws DAOException {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to update Order");
        }
    }

    @Override
    public void removeEntry(Order entry) throws DAOException {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove Order");
        }
    }

    @Override
    public Order findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(Order.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Order");
        }
    }

    @Override
    public List<Order> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), Order.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Order");
        }
    }

    @Override
    public List<Order> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from Order", Order.class).getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Order");
        }
    }
}
