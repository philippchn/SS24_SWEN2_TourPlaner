package org.technikum.tourplaner.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.technikum.tourplaner.models.TourModel;

public class TourRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourRepository(){
        entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate_tour");
    }
    public void save(TourModel tour){
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tour);
            transaction.commit();
        }
    }
}
