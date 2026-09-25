package data.DAO;

import data.entities.*;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDAO implements DAO<Product> {
    @Override
    public void addEntry(Product entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to add Product");
        }
    }

    @Override
    public void overwriteEntry(Product newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to update Product");
        }
    }

    @Override
    public void removeEntry(Product entry) {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove Product");
        }
    }

    @Override
    public Product findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(Product.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Product");
        }
    }

    @Override
    public List<Product> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), Product.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Failed to find Product");
        }
    }

    @Override
    public List<Product> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from Product", Product.class).getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove Product");
        }
    }
}
