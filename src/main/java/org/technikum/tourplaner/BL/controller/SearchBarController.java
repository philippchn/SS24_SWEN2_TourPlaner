package org.technikum.tourplaner.BL.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.technikum.tourplaner.BL.viewmodels.SearchViewModel;
import org.technikum.tourplaner.BL.viewmodels.TourViewModel;

public class SearchBarController {
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;

    @Setter
    private SearchViewModel searchViewModel;

    @Setter
    private TourViewModel tourViewModel;

    public SearchBarController(TourViewModel tourViewModel) {
        this.tourViewModel = tourViewModel;
    }

    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> performSearch());
        searchTextField.setOnAction(event -> performSearch());
        refreshButton.setOnAction(event -> refresh());
    }

    private void performSearch() {
        String query = searchTextField.getText();
        if (query != null && !query.trim().isEmpty()) {
            searchViewModel.performSearch(query.trim());
        }
    }

    private void refresh() {
        searchTextField.clear();
        tourViewModel.loadToursFromDatabase();
    }
}
