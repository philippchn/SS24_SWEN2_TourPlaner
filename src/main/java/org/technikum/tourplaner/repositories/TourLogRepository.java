package org.technikum.tourplaner.repositories;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.models.TourLogModel;

import java.util.List;

public class TourLogRepository {
    private static final Logger logger = LogManager.getLogger(TourLogRepository.class);

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
            logger.info("Saved tourLog to DB: " + tourLog.getObjectStringView());
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
            logger.info("TourLog with id " + id + " updated to " + updatedTourLog.getObjectStringView());
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
            logger.info("TourLog with id " + id + " deleted");
        }
    }

    public List<TourLogModel> getTourLogsByTourId(String tourId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Query query = entityManager.createQuery("SELECT t FROM TourLogModel t WHERE t.tourId = :tourId");
            query.setParameter("tourId", tourId);
            logger.info("TourLog with id " + tourId + " found");
            return query.getResultList();
        }
    }

    public List<TourLogModel> searchTourLogs(String query) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String searchQuery = "%" + query.toLowerCase() + "%";
            Query q = entityManager.createQuery(
                    "SELECT t FROM TourLogModel t " +
                            "WHERE LOWER(t.date) LIKE :query " +
                            "OR LOWER(t.comment) LIKE :query " +
                            "OR LOWER(t.difficulty) LIKE :query " +
                            "OR LOWER(t.totalDistance) LIKE :query " +
                            "OR LOWER(t.totalTime) LIKE :query " +
                            "OR LOWER(t.rating) LIKE :query"
            );
            q.setParameter("query", searchQuery);
            logger.info("Search for TourLogs with query: " + query);
            return q.getResultList();
        }
    }

    public List<TourLogModel> getAllTourLogs() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Query query = entityManager.createQuery("SELECT t FROM TourLogModel t");
            logger.info("Fetching all TourLogs from the database");
            return query.getResultList();
        }
    }

}
