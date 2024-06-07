package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

public class ModifyTourPopupController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private TextField transportTypeTextField;
    @FXML
    private Button saveButton;

    private TourModel selectedTour;
    private Stage stage;
    private TourViewModel tourViewModel;

    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty descriptionProperty = new SimpleStringProperty();
    private StringProperty fromProperty = new SimpleStringProperty();
    private StringProperty toProperty = new SimpleStringProperty();
    private StringProperty transportTypeProperty = new SimpleStringProperty();

    @FXML
    private void initialize() {
        bindProperties();
        saveButton.setOnAction(event -> saveTour());
    }

    public void initData(TourModel selectedTour, Stage stage, TourViewModel tourViewModel) {
        this.selectedTour = selectedTour;
        this.stage = stage;
        this.tourViewModel = tourViewModel;
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(nameProperty);
        descriptionTextField.textProperty().bindBidirectional(descriptionProperty);
        fromTextField.textProperty().bindBidirectional(fromProperty);
        toTextField.textProperty().bindBidirectional(toProperty);
        transportTypeTextField.textProperty().bindBidirectional(transportTypeProperty);
    }

    @FXML
    private void saveTour() {
        selectedTour.setName(nameProperty.get());
        selectedTour.setTourDescription(descriptionProperty.get());
        selectedTour.setFrom(fromProperty.get());
        selectedTour.setTo(toProperty.get());
        selectedTour.setTransportType(transportTypeProperty.get());

        stage.close();
    }
}
