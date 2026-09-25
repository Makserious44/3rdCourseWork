package data.DAO;

import data.entities.User;
import data.misc.StoredQuery;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO implements DAO<User> {
    @Override
    public void addEntry(User entry) {
        if (entry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(entry);
            transaction.commit();
        }
        catch (Exception e) {
            throw new DAOException("Failed to add User: " + e.getMessage());
        }
    }

    @Override
    public void overwriteEntry(User newEntry) {
        if (newEntry == null)
            throw new DAOException("Entry is null");

        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            Transaction transaction = session.beginTransaction();
//            String hql = """
//                    update User u SET
//                    u.login = :login,
//                    u.password = :password,
//                    u.role = :role,
//                    u.userInfo.firstName = :firstName,
//                    u.userInfo.lastName = :lastName,
//                    u.userInfo.phone = :phone,
//                    u.userInfo.mail = :mail,
//                    u.userInfo.address = :address
//                    where u.id = :userId
//                    """;
//
//            session.createQuery(hql)
//                    .setParameter("login", newEntry.getLogin())
//                    .setParameter("password", newEntry.getPassword())
//                    .setParameter("role", newEntry.getRole())
//                    .setParameter("firstName", newEntry.getUserInfo().getFirstName())
//                    .setParameter("lastName", newEntry.getUserInfo().getLastName())
//                    .setParameter("phone", newEntry.getUserInfo().getPhone())
//                    .setParameter("mail", newEntry.getUserInfo().getMail())
//                    .setParameter("address", newEntry.getUserInfo().getAddress())
//                    .setParameter("userId", newEntry.getId())
//                    .executeUpdate();


//            session.detach(newEntry);
//            session.detach(newEntry.getUserInfo());
            session.merge(newEntry);
            transaction.commit();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("Failed to update User");
        }
    }

    @Override
    public void removeEntry(User entry) {
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
            throw new DAOException("Failed to remove User");
        }
    }

    @Override
    public User findEntry(int id) {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.find(User.class, id);
        }
        catch (Exception e) {
            throw new DAOException("Failed to find User");
        }
    }

    @Override
    public List<User> getByQuery(StoredQuery criteria) {
        System.out.println(criteria);
        try (Session session = DBSessionFactory.getSessionFactory().openSession()) {
            Query query = session.createQuery(criteria.hql(), User.class);
            criteria.parameters().forEach(query::setParameter);
            return query.getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find User");
        }
    }

    @Override
    public List<User> getAllEntries() {
        try (Session session = DBSessionFactory.getSessionFactory().openSession())
        {
            return session.createQuery("from User", User.class).getResultList();
        }
        catch (Exception e) {
            throw new DAOException("Failed to find User");
        }
    }
}
