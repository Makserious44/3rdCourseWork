package data.DAO;

import data.entities.Order;
import data.entities.UserInfo;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserInfoDAO implements DAO<UserInfo> {
    @Override
    public void addEntry(UserInfo entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to add User Info");
        }
    }

    @Override
    public void overwriteEntry(UserInfo newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to update User Info");
        }
    }

    @Override
    public void removeEntry(UserInfo entry) {
        if (entry == null)
            throw new DAOException("Entry is full");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.remove(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove User Info");
        }
    }

    @Override
    public UserInfo findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(UserInfo.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find User info");
        }
    }

    @Override
    public List<UserInfo> getByQuery(StoredQuery criteria) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), UserInfo.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find User Info");
        }
    }

    @Override
    public List<UserInfo> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from UserInfo ", UserInfo.class).getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to remove User Info");
        }
    }
}
