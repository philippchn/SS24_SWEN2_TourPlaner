package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TourLogsController {
    @FXML
    private Label currentlySelectedTour;

    private final SimpleStringProperty selectedTourProperty = new SimpleStringProperty();

    @FXML
    private void initialize() {
        bindProperties();
    }

    private void bindProperties() {
        currentlySelectedTour.textProperty().bindBidirectional(selectedTourProperty);
    }

    public void setText(String text)
    {
        currentlySelectedTour.setText(text);
    }
}
