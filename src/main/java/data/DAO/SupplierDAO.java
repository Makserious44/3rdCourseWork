package data.DAO;

import data.entities.Supplier;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SupplierDAO implements DAO<Supplier> {
    @Override
    public void addEntry(Supplier entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
//            session.merge(entry.getUser());
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
//            throw new DAOException("Failed to add Supplier: " + e.getMessage());
            throw new DAOException("Failed to add Supplier: ");
        }
    }

    @Override
    public void overwriteEntry(Supplier newEntry) {
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
            throw new DAOException("Failed to update Supplier");
        }
    }

    @Override
    public void removeEntry(Supplier entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove Supplier: " + e.getMessage());
        }
    }

    @Override
    public Supplier findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(Supplier.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find Supplier");
        }
    }

    @Override
    public List<Supplier> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), Supplier.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to find Supplier");
        }
    }

    @Override
    public List<Supplier> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from Supplier ", Supplier.class).getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to get Suppliers");
        }
    }
}
