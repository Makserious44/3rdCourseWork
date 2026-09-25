package data.DAO;

import data.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBSessionFactory {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .addAnnotatedClasses(
                                User.class,
                                UserInfo.class,
                                Order.class,
                                Product.class,
                                Request.class,
                                Supplier.class,
                                OrderProduct.class,
                                RequestProduct.class)
                        .buildSessionFactory();
            } catch (Exception e) {
                System.out.println("Failed to establish connection with database: " + e.getMessage());
            }
        }

        return sessionFactory;
    }
}
