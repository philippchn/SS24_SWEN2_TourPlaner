package org.technikum.tourplaner.DAL.repositories;

public class RepositoryFactory {
    public RepositoryFactory(){
    }

    public TourRepository createTourRepository(){
        return new TourRepository();
    }

    public TourLogRepository createTourLogRepository(){
        return new TourLogRepository();
    }
}
