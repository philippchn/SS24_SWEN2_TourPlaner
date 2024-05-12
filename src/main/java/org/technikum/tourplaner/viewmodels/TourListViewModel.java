package org.technikum.tourplaner.viewmodels;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.technikum.tourplaner.models.TourModel;

public class TourListViewModel {
    private ObservableList<TourModel> tours = FXCollections.observableArrayList();

    public ObservableList<TourModel> getTours()
    {
        return tours;
    }

    public void addTour(TourModel tour) {
        tours.add(tour);
    }

    public void addListener(ListChangeListener<? super TourModel> listener) {
        tours.addListener(listener);
    }
}
