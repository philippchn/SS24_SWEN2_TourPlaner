package org.technikum.tourplaner.viewmodels;

import javafx.scene.control.Alert;

public class SearchViewModel {
    private final TourViewModel tourViewModel;
    private final TourLogViewModel tourLogViewModel;

    public SearchViewModel(TourViewModel tourViewModel, TourLogViewModel tourLogViewModel) {
        this.tourViewModel = tourViewModel;
        this.tourLogViewModel = tourLogViewModel;
    }

    public void performSearch(String query) {
        boolean foundInTours = tourViewModel.searchTours(query);
        boolean foundInTourLogs = tourLogViewModel.searchTourLogs(query);

        if (!foundInTours && !foundInTourLogs) {
            showAlert("No Results", "No matching tours or tour logs found for the query: " + query);
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
