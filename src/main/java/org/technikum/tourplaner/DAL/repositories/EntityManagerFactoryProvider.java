package org.technikum.tourplaner.DAL.repositories;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.hibernate.cfg.JdbcSettings.*;

public class EntityManagerFactoryProvider {
    private static EntityManagerFactory tourEntityManagerFactory;
    private static EntityManagerFactory tourLogEntityManagerFactory;

    public static EntityManagerFactory getTourEntityManagerFactory() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cfg");
        String user = resourceBundle.getString("dbUser");
        String password = resourceBundle.getString("dbPassword");

        if (tourEntityManagerFactory == null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
            properties.put(JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/db_tourplanner");
            properties.put(JAKARTA_JDBC_USER, user);
            properties.put(JAKARTA_JDBC_PASSWORD, password);
            properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            properties.put(Environment.HBM2DDL_AUTO, "update");

            tourEntityManagerFactory = new org.hibernate.jpa.HibernatePersistenceProvider()
                    .createEntityManagerFactory("hibernate_tour", properties);
        }
        return tourEntityManagerFactory;
    }

    public static EntityManagerFactory getTourLogEntityManagerFactory() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cfg");
        String user = resourceBundle.getString("dbUser");
        String password = resourceBundle.getString("dbPassword");

        if (tourLogEntityManagerFactory == null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
            properties.put(JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/db_tourplanner");
            properties.put(JAKARTA_JDBC_USER, user);
            properties.put(JAKARTA_JDBC_PASSWORD, password);
            properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            properties.put(Environment.HBM2DDL_AUTO, "update");

            tourLogEntityManagerFactory = new org.hibernate.jpa.HibernatePersistenceProvider()
                    .createEntityManagerFactory("hibernate_tourLog", properties);
        }
        return tourLogEntityManagerFactory;
    }

    public static void closeEntityManagerFactories() {
        if (tourEntityManagerFactory != null && tourEntityManagerFactory.isOpen()) {
            tourEntityManagerFactory.close();
        }
        if (tourLogEntityManagerFactory != null && tourLogEntityManagerFactory.isOpen()) {
            tourLogEntityManagerFactory.close();
        }
    }
}
