package org.technikum.tourplaner.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

public class MainViewController {
    @FXML
    private VBox mainView;

    private final TourViewModel tourViewModel;
    private final TourLogViewModel tourLogViewModel;


    public MainViewController(TourViewModel tourViewModel, TourLogViewModel tourLogViewModel) {
        this.tourViewModel = tourViewModel;
        this.tourLogViewModel = tourLogViewModel;
    }
}

