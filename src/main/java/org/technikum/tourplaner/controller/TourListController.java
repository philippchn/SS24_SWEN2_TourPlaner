package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.technikum.tourplaner.entity.Tour;
import org.technikum.tourplaner.repository.TourRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourListController {

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
    private ListView<Tour> tourListView; // Add ListView for tours

    private Tour currentTour = new Tour(); // Currently edited tour
    private final SimpleStringProperty nameProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleStringProperty fromProperty = new SimpleStringProperty();
    private final SimpleStringProperty toProperty = new SimpleStringProperty();
    private final SimpleStringProperty transportTypeProperty = new SimpleStringProperty();

    private TourRepo tourRepo = new TourRepo();

    @FXML
    private void initialize() {
        bindProperties();
        // Add event handler to the save button
        saveButton.setOnAction(event -> saveTour());
        statusMessageLabel.setText("Create new tour:");
        statusMessageLabel.setTextFill(Color.BLACK);
    }

    private void saveTour() {
        if (isValidInput()) {

            Tour newTour = new Tour(
                    nameTextField.getText().trim(),
                    descriptionTextField.getText().trim(),
                    fromTextField.getText().trim(),
                    toTextField.getText().trim(),
                    transportTypeTextField.getText().trim()
            );

            statusMessageLabel.setText("processing Request....");
            tourRepo.saveTour(newTour);
            statusMessageLabel.setText("Request processed");

            // Clear input fields after saving
            clearFields();

            // Add the saved tour to the list in the "Tours" tab
            tourListView.getItems().add(newTour);

        }
    }

    private boolean isValidInput() {
        boolean isValidInput = true;
        String newStatusMessage = "";

        // Check for empty input
        if (nameTextField.getText().trim().isEmpty()) {
            newStatusMessage = "'Tour Name' must not be empty";
            isValidInput = false;
        } else if (fromTextField.getText().trim().isEmpty()) {
            newStatusMessage = "'Origin' must not be empty";
            isValidInput = false;
        } else if (toTextField.getText().trim().isEmpty()) {
            newStatusMessage = "'Destination' must not be empty";
            isValidInput = false;
        }

        if (isValidInput) {
            Pattern pattern = Pattern.compile("^[a-zA-ZäÄöÖüÜ]+$");
            Matcher validateFrom = pattern.matcher(fromTextField.getText().trim());
            Matcher validateTo = pattern.matcher(toTextField.getText().trim());

            if (!(validateFrom.matches() && validateTo.matches())) {
                newStatusMessage = "Origin and destination must not contain numbers or special characters";
                isValidInput = false;
            }
        }

        if (!isValidInput) {
            statusMessageLabel.setTextFill(Color.RED);
            statusMessageLabel.setText(newStatusMessage);
        }

        return isValidInput;
    }

    private void clearFields() {
        nameTextField.clear();
        descriptionTextField.clear();
        fromTextField.clear();
        toTextField.clear();
        transportTypeTextField.clear();
        statusMessageLabel.setText(""); // Clear error message
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(nameProperty);
        descriptionTextField.textProperty().bindBidirectional(descriptionProperty);
        fromTextField.textProperty().bindBidirectional(fromProperty);
        toTextField.textProperty().bindBidirectional(toProperty);
        transportTypeTextField.textProperty().bindBidirectional(transportTypeProperty);
    }
}
