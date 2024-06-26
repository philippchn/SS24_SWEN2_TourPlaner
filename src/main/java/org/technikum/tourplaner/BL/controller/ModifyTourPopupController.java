package org.technikum.tourplaner.BL.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.technikum.tourplaner.BL.models.TourModel;
import org.technikum.tourplaner.BL.viewmodels.TourViewModel;
import org.technikum.tourplaner.DAL.openrouteservice.ETransportType;

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
    private ComboBox<ETransportType> transportTypeBox;
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

    public ModifyTourPopupController(TourViewModel tourViewModel) {
        this.tourViewModel = tourViewModel;
    }

    @FXML
    private void initialize() {
        bindProperties();
        saveButton.setOnAction(event -> saveTour());
        transportTypeBox.getItems().addAll(ETransportType.values());
    }

    public void initData(TourModel selectedTour, Stage stage) {
        this.selectedTour = selectedTour;
        this.stage = stage;

        nameProperty.set(selectedTour.getName());
        descriptionProperty.set(selectedTour.getTourDescription());
        fromProperty.set(selectedTour.getFrom());
        toProperty.set(selectedTour.getTo());
        transportTypeProperty.set(selectedTour.getTransportType());
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(tourViewModel.nameProperty());
        descriptionTextField.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
        fromTextField.textProperty().bindBidirectional(tourViewModel.fromProperty());
        toTextField.textProperty().bindBidirectional(tourViewModel.toProperty());
        transportTypeBox.valueProperty().bindBidirectional(tourViewModel.transportTypeProperty());
    }

    @FXML
    private void saveTour() {
        if(!tourViewModel.allFieldsSet()) {
            return;
        }

        selectedTour.setName(nameProperty.get());
        selectedTour.setTourDescription(descriptionProperty.get());
        selectedTour.setFrom(fromProperty.get());
        selectedTour.setTo(toProperty.get());
        selectedTour.setTransportType(transportTypeProperty.get());

        stage.close();
    }
}
