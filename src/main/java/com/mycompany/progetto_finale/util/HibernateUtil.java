package com.mycompany.progetto_finale.util;

import com.mycompany.progetto_finale.model.Category;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public final class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static String databasePath = "src/main/webapp/northwind.db";

    private HibernateUtil() {
    }

    public static synchronized void setDatabasePath(String path) {
        databasePath = path;
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.JAKARTA_JDBC_DRIVER, "org.sqlite.JDBC");
            settings.put(Environment.JAKARTA_JDBC_URL, "jdbc:sqlite:" + databasePath);
            settings.put(Environment.DIALECT, "org.hibernate.community.dialect.SQLiteDialect");
            settings.put(Environment.SHOW_SQL, "false");
            settings.put(Environment.FORMAT_SQL, "true");
            settings.put(Environment.HBM2DDL_AUTO, "none");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(Category.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}
