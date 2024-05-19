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

    public void updateTourLog(TourLogModel updatedTourLog) {
        for (int i = 0; i < tourLogModelList.size(); i++) {
            TourLogModel tourLog = tourLogModelList.get(i);
            if (tourLog.getComment().equals(updatedTourLog.getComment())) { // used comment here for some unique feature but we will use id when we implement the db
                tourLogModelList.set(i, updatedTourLog);
                break;
            }
        }
    }

    public void deleteTourLog(TourLogModel tourLogToDelete) {
        tourLogModelList.removeIf(tourLog -> tourLog.getComment().equals(tourLogToDelete.getComment()));
    }
}
