package org.technikum.tourplaner.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.technikum.tourplaner.models.TourLogModel;

public class TourLogRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourLogRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate_tourLog");
    }

    public void save(TourLogModel tourLog) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tourLog);
            transaction.commit();
        }
    }

    public void updateById(Long id, TourLogModel updatedTourLog) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            TourLogModel existingTourLog = entityManager.find(TourLogModel.class, id);
            if (existingTourLog != null) {
                existingTourLog.setDate(updatedTourLog.getDate());
                existingTourLog.setComment(updatedTourLog.getComment());
                existingTourLog.setDifficulty(updatedTourLog.getDifficulty());
                existingTourLog.setTotalDistance(updatedTourLog.getTotalDistance());
                existingTourLog.setTotalTime(updatedTourLog.getTotalTime());
                existingTourLog.setRating(updatedTourLog.getRating());
                existingTourLog.setTourId(updatedTourLog.getTourId());

                entityManager.merge(existingTourLog);
            }
            transaction.commit();
        }
    }

    public void deleteById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            TourLogModel tourLog = entityManager.find(TourLogModel.class, id);
            if (tourLog != null) {
                entityManager.remove(tourLog);
            }
            transaction.commit();
        }
    }

    public TourLogModel getUpdatedTourLog(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.find(TourLogModel.class, id);
        }
    }

}
