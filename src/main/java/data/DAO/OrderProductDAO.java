package data.DAO;

import data.entities.OrderProduct;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderProductDAO implements DAO<OrderProduct> {
    @Override
    public void addEntry(OrderProduct entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Failed to add OrderProduct: " + e.getMessage());
        }
    }

    @Override
    public void overwriteEntry(OrderProduct newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to update OrderProduct");
        }
    }

    @Override
    public void removeEntry(OrderProduct entry) {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove OrderProduct");
        }
    }

    @Override
    public OrderProduct findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(OrderProduct.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find OrderProduct");
        }
    }

    @Override
    public List<OrderProduct> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), OrderProduct.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Failed to find OrderProduct");
        }
    }

    @Override
    public List<OrderProduct> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from OrderProduct ", OrderProduct.class).getResultList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to find OrderProduct");
        }
    }
}
