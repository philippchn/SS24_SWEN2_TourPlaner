package org.technikum.tourplaner.DAL.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.models.TourLogModel;

import java.util.ArrayList;
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

    public List<TourLogModel> getAllTourLogs() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Query query = entityManager.createQuery("SELECT t FROM TourLogModel t");
            logger.info("Fetching all TourLogs from the database");
            return query.getResultList();
        }
    }

    public List<TourLogModel> searchTourLogs(String query) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourLogModel> criteriaQuery = criteriaBuilder.createQuery(TourLogModel.class);
            Root<TourLogModel> root = criteriaQuery.from(TourLogModel.class);

            String searchPattern = "%" + query.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), searchPattern));

            try {
                int intQuery = Integer.parseInt(query);
                predicates.add(criteriaBuilder.equal(root.get("difficulty"), intQuery));
                predicates.add(criteriaBuilder.equal(root.get("rating"), intQuery));
            } catch (NumberFormatException e) {
                logger.warn("Query '{}' is not a valid integer: {}", query, e.getMessage());
            }

            try {
                double doubleQuery = Double.parseDouble(query);
                predicates.add(criteriaBuilder.equal(root.get("totalDistance"), doubleQuery));
                predicates.add(criteriaBuilder.equal(root.get("totalTime"), (long) doubleQuery));
            } catch (NumberFormatException e) {
                logger.warn("Query '{}' is not a valid double: {}", query, e.getMessage());
            }

            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

            List<TourLogModel> tourLogs = entityManager.createQuery(criteriaQuery).getResultList();

            logger.info("Found " + tourLogs.size() + " TourLogs for query: " + query);

            return tourLogs;
        }
    }
}
