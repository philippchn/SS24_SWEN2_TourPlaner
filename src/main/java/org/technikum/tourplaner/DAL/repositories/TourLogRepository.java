package org.technikum.tourplaner.DAL.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.models.TourLogModel;

import java.util.List;

public class TourLogRepository implements Repository{
    private static final Logger logger = LogManager.getLogger(TourLogRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    TourLogRepository() {
        entityManagerFactory = EntityManagerFactoryProvider.getTourLogEntityManagerFactory();
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

    public void deleteByFK(Long fkId){
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            String jpql = "DELETE FROM TourLogModel  t WHERE t.tourId = :fkId";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("fkId", fkId);
            int deletedCount = query.executeUpdate();
            transaction.commit();
            logger.info("TourLog with FK " + fkId + " deleted. Total Logs deleted: " + deletedCount);
        }
    }
}
