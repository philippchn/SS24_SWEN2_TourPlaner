package org.technikum.tourplaner.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.technikum.tourplaner.models.TourModel;

public class TourViewModel {
    private ObservableList<TourModel> tours = FXCollections.observableArrayList();
    private ObjectProperty<TourModel> selectedTourModel = new SimpleObjectProperty<>();

    public ObservableList<TourModel> getTours()
    {
        return tours;
    }

    public void addTour(TourModel tour) {
        tours.add(tour);
    }

    public TourModel getSelectedTourModel() {
        return selectedTourModel.get();
    }

    public void setSelectedTourModel(TourModel tour) {
        this.selectedTourModel.set(tour);
        System.out.println("Selected: " + selectedTourModel.getValue() + " from list");
    }

    public ObjectProperty<TourModel> selectedTourModelProperty() {
        return selectedTourModel;
    }
}
