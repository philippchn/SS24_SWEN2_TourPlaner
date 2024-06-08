package org.technikum.tourplaner.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.technikum.tourplaner.models.TourModel;

public class TourRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate_tour");
    }

    public void save(TourModel tour) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(tour);
            transaction.commit();
        }
    }

    public void updateById(Long id, TourModel updatedTour) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            TourModel existingTour = entityManager.find(TourModel.class, id);
            if (existingTour != null) {
                // Update fields
                existingTour.setName(updatedTour.getName());
                existingTour.setTourDescription(updatedTour.getTourDescription());
                existingTour.setFrom(updatedTour.getFrom());
                existingTour.setTo(updatedTour.getTo());
                existingTour.setTransportType(updatedTour.getTransportType());
                existingTour.setDistance(updatedTour.getDistance());
                existingTour.setEstimatedTime(updatedTour.getEstimatedTime());
                existingTour.setRouteInformation(updatedTour.getRouteInformation());
                existingTour.setTourLogsMap(updatedTour.getTourLogsMap()); // Transient but useful

                entityManager.merge(existingTour);
            }
            transaction.commit();
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
        }
    }
}
