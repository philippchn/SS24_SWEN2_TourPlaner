package org.technikum.tourplaner.repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.technikum.tourplaner.entity.Tour;

public class TourRepo {
    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();


    public void saveTour(Tour tour) {
        tourList.add(tour);
    }
}
