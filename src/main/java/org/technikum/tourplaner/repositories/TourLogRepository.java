package org.technikum.tourplaner.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.technikum.tourplaner.models.TourLogModel;

public class TourLogRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourLogRepository(){
        entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate_tourLog");
    }

    public void save(TourLogModel tourLog){
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tourLog);
            transaction.commit();
        }
    }
}
