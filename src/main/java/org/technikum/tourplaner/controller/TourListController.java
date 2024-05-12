package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourListViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourListController {
    @FXML
    private VBox rootVbox;

    @FXML
    private TabPane tabPane;

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

    @FXML
    private Label statusMessageLabel;

    @FXML
    private ListView<TourModel> tourListView;

    private final SimpleStringProperty nameProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleStringProperty fromProperty = new SimpleStringProperty();
    private final SimpleStringProperty toProperty = new SimpleStringProperty();
    private final SimpleStringProperty transportTypeProperty = new SimpleStringProperty();

    private final TourListViewModel tourListViewModel;

    public TourListController() {
        this.tourListViewModel = new TourListViewModel();
    }

    @FXML
    private void initialize() {
        bindProperties();
        saveButton.setOnAction(event -> saveTour());
        tourListView.setOnMouseClicked(event -> clickElement());

        tourListView.setItems(tourListViewModel.getTours());
    }

    private void saveTour() {
        if (isValidInput()) {
            TourModel newTour = new TourModel(
                    nameTextField.getText().trim(),
                    descriptionTextField.getText().trim(),
                    fromTextField.getText().trim(),
                    toTextField.getText().trim(),
                    transportTypeTextField.getText().trim()
            );

            statusMessageLabel.setText("processing Request....");
            tourListViewModel.addTour(newTour);
            statusMessageLabel.setText("Request processed");

            clearInputFields();
        }
    }

    private boolean isValidInput() {
        if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
            setErrorMessage(nameTextField);
            return false;
        } else if (fromTextField.getText() == null || fromTextField.getText().isBlank()) {
            setErrorMessage(fromTextField);
            return false;
        } else if (toTextField.getText() == null || toTextField.getText().isBlank()) {
            setErrorMessage(toTextField);
            return false;
        } else if (transportTypeTextField.getText() == null || transportTypeTextField.getText().isBlank()) {
            setErrorMessage(transportTypeTextField);
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-ZäÄöÖüÜ]+$");
        Matcher validateFrom = pattern.matcher(fromTextField.getText().trim());
        Matcher validateTo = pattern.matcher(toTextField.getText().trim());

        if (!(validateFrom.matches() && validateTo.matches())) {
            statusMessageLabel.setTextFill(Color.RED);
            statusMessageLabel.setText("From and To must not contain numbers or special characters");
            return false;
        }

        statusMessageLabel.setTextFill(Color.BLACK);
        return true;
    }

    private void setErrorMessage(TextField missingTextField)
    {
        statusMessageLabel.setTextFill(Color.RED);
        statusMessageLabel.setText(missingTextField.getPromptText() + " must not be empty");
    }

    private void clearInputFields() {
        nameTextField.clear();
        descriptionTextField.clear();
        fromTextField.clear();
        toTextField.clear();
        transportTypeTextField.clear();
        statusMessageLabel.setText("Create new tour:"); // Clear error message
    }

    // TODO
    private void clickElement() {
        System.out.println(tourListView.getSelectionModel().getSelectedItem());
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(nameProperty);
        descriptionTextField.textProperty().bindBidirectional(descriptionProperty);
        fromTextField.textProperty().bindBidirectional(fromProperty);
        toTextField.textProperty().bindBidirectional(toProperty);
        transportTypeTextField.textProperty().bindBidirectional(transportTypeProperty);
    }
}
