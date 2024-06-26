package org.technikum.tourplaner.DAL.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.models.TourModel;

import java.util.List;

public class TourRepository implements Repository{
    private static final Logger logger = LogManager.getLogger(TourRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    TourRepository() {
        entityManagerFactory = EntityManagerFactoryProvider.getTourEntityManagerFactory();
    }

    public Long save(TourModel tour) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tour);
            transaction.commit();
            logger.info("Saved tour to db: " + tour.getObjectStringView());
            return tour.getId();
        }
    }

    public void updateById(Long id, TourModel updatedTour) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            TourModel existingTour = entityManager.find(TourModel.class, id);
            if (existingTour != null) {
                existingTour.setName(updatedTour.getName());
                existingTour.setTourDescription(updatedTour.getTourDescription());
                existingTour.setFrom(updatedTour.getFrom());
                existingTour.setTo(updatedTour.getTo());
                existingTour.setTransportType(updatedTour.getTransportType());
                existingTour.setDistance(updatedTour.getDistance());
                existingTour.setEstimatedTime(updatedTour.getEstimatedTime());
                existingTour.setRouteInformation(updatedTour.getRouteInformation());
                existingTour.setTourLogsMap(updatedTour.getTourLogsMap());

                entityManager.merge(existingTour);
            }
            transaction.commit();
            logger.info("Tour with id " + id + " updated to " + updatedTour.getObjectStringView());
        }
    }

    public void deleteById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            TourModel tour = entityManager.find(TourModel.class, id);
            if (tour != null) {
                entityManager.remove(tour);
            }
            transaction.commit();
            logger.info("Tour with id " + id + " deleted");
        }
    }

    public List<TourModel> getAllTours() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourModel> criteriaQuery = criteriaBuilder.createQuery(TourModel.class);
            Root<TourModel> root = criteriaQuery.from(TourModel.class);
            criteriaQuery.select(root);

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }
}
