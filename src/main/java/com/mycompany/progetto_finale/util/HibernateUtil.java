package com.mycompany.progetto_finale;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	private static String filePath = "northwind.db";
	
	public static void SetFilePath(String path) {
		filePath = path;
	}
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				// Hibernate settings equivalent to hibernate.cfg.xml's properties
				Properties settings = new Properties();
				settings.put(Environment.JAKARTA_JDBC_DRIVER, "org.sqlite.JDBC");
				settings.put(Environment.JAKARTA_JDBC_URL, "jdbc:sqlite:" + filePath);

				settings.put(Environment.DIALECT, "org.hibernate.community.dialect.SQLiteDialect");

				settings.put(Environment.SHOW_SQL, "true");

				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

				settings.put(Environment.HBM2DDL_AUTO, "none");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(Catalogo.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				System.out.println("Hibernate Java Config serviceRegistry created");
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				return sessionFactory;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}
