package org.technikum.tourplaner.BL.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.viewmodels.TourLogViewModel;

import java.time.LocalDate;

public class ModifyTourLogsPopupController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField commentTextField;
    @FXML
    private TextField difficultyTextField;
    @FXML
    private TextField totalDistanceTextField;
    @FXML
    private TextField totalTimeTextField;
    @FXML
    private TextField ratingTextField;
    @FXML
    private Button saveButton;

    private TourLogModel selectedTourLog;
    private Stage stage;
    private TourLogViewModel tourLogViewModel;

    private final ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final StringProperty commentProperty = new SimpleStringProperty();
    private final StringProperty difficultyProperty = new SimpleStringProperty();
    private final StringProperty totalDistanceProperty = new SimpleStringProperty();
    private final StringProperty totalTimeProperty = new SimpleStringProperty();
    private final StringProperty ratingProperty = new SimpleStringProperty();

    @FXML
    private void initialize() {
        bindProperties();
        saveButton.setOnAction(event -> saveTourLog());
    }

    public void initData(TourLogModel selectedTourLog, Stage stage, TourLogViewModel tourLogViewModel) {
        this.selectedTourLog = selectedTourLog;
        this.stage = stage;
        this.tourLogViewModel = tourLogViewModel;

        dateProperty.set(LocalDate.from(selectedTourLog.getDate()));
        commentProperty.set(selectedTourLog.getComment());
        difficultyProperty.set(String.valueOf(selectedTourLog.getDifficulty()));
        totalDistanceProperty.set(String.valueOf(selectedTourLog.getTotalDistance()));
        totalTimeProperty.set(String.valueOf(selectedTourLog.getTotalTime()));
        ratingProperty.set(String.valueOf(selectedTourLog.getRating()));
    }

    private void bindProperties() {
        datePicker.valueProperty().bindBidirectional(dateProperty);

        commentTextField.textProperty().bindBidirectional(commentProperty);
        difficultyTextField.textProperty().bindBidirectional(difficultyProperty);
        totalDistanceTextField.textProperty().bindBidirectional(totalDistanceProperty);
        totalTimeTextField.textProperty().bindBidirectional(totalTimeProperty);
        ratingTextField.textProperty().bindBidirectional(ratingProperty);
    }

    @FXML
    private void saveTourLog() {
        if (!isValidInput()) {
            return;
        }

        selectedTourLog.setDate(LocalDate.from(dateProperty.get()));
        selectedTourLog.setComment(commentProperty.get());
        selectedTourLog.setDifficulty(Integer.valueOf(difficultyProperty.get()));
        selectedTourLog.setTotalDistance(Double.valueOf(totalDistanceProperty.get()));
        selectedTourLog.setTotalTime(Long.valueOf(totalTimeProperty.get()));
        selectedTourLog.setRating(Integer.valueOf(ratingProperty.get()));

        tourLogViewModel.updateTourLog(selectedTourLog);

        stage.close();
    }

    private boolean isValidInput() {
        String errorMessage = "";

        if (datePicker.getValue() == null) {
            errorMessage += "Date must be specified.\n";
        }
        if (commentTextField.getText() == null || commentTextField.getText().trim().isEmpty()) {
            errorMessage += "Comment must be specified.\n";
        }
        try {
            int difficulty = Integer.parseInt(difficultyTextField.getText());
            if (difficulty <= 0 || difficulty > 10) {
                errorMessage += "Difficulty must be between 1 and 10.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Difficulty must be a valid integer.\n";
        }
        try {
            double totalDistance = Double.parseDouble(totalDistanceTextField.getText());
            if (totalDistance < 0) {
                errorMessage += "Total distance must be a non-negative number.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Total distance must be a valid number.\n";
        }
        try {
            long totalTime = Long.parseLong(totalTimeTextField.getText());
            if (totalTime < 0) {
                errorMessage += "Total time must be a non-negative number.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Total time must be a valid integer.\n";
        }
        try {
            int rating = Integer.parseInt(ratingTextField.getText());
            if (rating < 0) {
                errorMessage += "Rating must be a non-negative number.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Rating must be a valid integer.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
