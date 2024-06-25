package org.technikum.tourplaner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.technikum.tourplaner.viewmodels.SearchViewModel;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

public class SearchBarController {
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    @Setter
    private SearchViewModel searchViewModel;

    public SearchBarController() {
        // No-args constructor for FXMLLoader
    }

    @FXML
    private void initialize() {
        searchButton.setOnAction(event -> performSearch());
        searchTextField.setOnAction(event -> performSearch()); // Trigger search on Enter key
    }

    private void performSearch() {
        String query = searchTextField.getText();
        searchViewModel.performSearch(query);
    }

}
