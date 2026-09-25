package data.DAO;

import data.entities.RequestProduct;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RequestProductDAO implements DAO<RequestProduct> {
    @Override
    public void addEntry(RequestProduct entry) {
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
            throw new DAOException("Failed to add RequestProduct: " + e.getMessage());
        }
    }

    @Override
    public void overwriteEntry(RequestProduct newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to update RequestProduct");
        }
    }

    @Override
    public void removeEntry(RequestProduct entry) {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove RequestProduct");
        }
    }

    @Override
    public RequestProduct findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(RequestProduct.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find RequestProduct");
        }
    }

    @Override
    public List<RequestProduct> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), RequestProduct.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Failed to find RequestProduct");
        }
    }

    @Override
    public List<RequestProduct> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from RequestProduct", RequestProduct.class).getResultList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to find RequestProducts");
        }
    }
}
