package org.technikum.tourplaner.viewmodels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.technikum.tourplaner.models.TourLogModel;

@Getter
public class TourLogViewModel {
    private final ObservableList<TourLogModel> tourLogModelList = FXCollections.observableArrayList();

    public void addTourLogModel(TourLogModel tourLogModel) {
        tourLogModelList.add(tourLogModel);
    }
}
