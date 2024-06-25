package org.technikum.tourplaner.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.models.TourModel;

import java.util.ArrayList;
import java.util.List;

public class TourRepository {
    private static final Logger logger = LogManager.getLogger(TourRepository.class);

    private final EntityManagerFactory entityManagerFactory;

    public TourRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate_tour");
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

    public List<TourModel> searchTours(String query) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourModel> criteriaQuery = criteriaBuilder.createQuery(TourModel.class);
            Root<TourModel> root = criteriaQuery.from(TourModel.class);

            String searchPattern = "%" + query.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tourDescription")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("from")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("to")), searchPattern));

            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<TourModel> getToursByIds(List<Integer> ids) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourModel> criteriaQuery = criteriaBuilder.createQuery(TourModel.class);
            Root<TourModel> root = criteriaQuery.from(TourModel.class);
            criteriaQuery.where(root.get("id").in(ids));

            List<TourModel> tours = entityManager.createQuery(criteriaQuery).getResultList();

            if (tours.isEmpty()) {
                logger.info("No tours found for the given IDs: " + ids);
            } else {
                logger.info("Found " + tours.size() + " tours for the given IDs: " + ids);
                for (TourModel tour : tours) {
                    logger.info("Found Tour ID: " + tour.getId() + ", Tour Name: " + tour.getName());
                }
            }

            return tours;
        } catch (Exception e) {
            logger.error("Error while fetching tours by IDs", e);
            return List.of(); // Return empty list in case of error
        }
    }

}
