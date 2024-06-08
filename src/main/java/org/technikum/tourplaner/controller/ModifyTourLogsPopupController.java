package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;

public class ModifyTourLogsPopupController {
    @FXML
    private TextField dateTextField;
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

    private StringProperty dateProperty = new SimpleStringProperty();
    private StringProperty commentProperty = new SimpleStringProperty();
    private StringProperty difficultyProperty = new SimpleStringProperty();
    private StringProperty totalDistanceProperty = new SimpleStringProperty();
    private StringProperty totalTimeProperty = new SimpleStringProperty();
    private StringProperty ratingProperty = new SimpleStringProperty();

    @FXML
    private void initialize() {
        bindProperties();
        saveButton.setOnAction(event -> saveTourLog());
    }

    public void initData(TourLogModel selectedTourLog, Stage stage, TourLogViewModel tourLogViewModel) {
        this.selectedTourLog = selectedTourLog;
        this.stage = stage;
        this.tourLogViewModel = tourLogViewModel;

        dateProperty.set(selectedTourLog.getDate());
        commentProperty.set(selectedTourLog.getComment());
        difficultyProperty.set(selectedTourLog.getDifficulty());
        totalDistanceProperty.set(selectedTourLog.getTotalDistance());
        totalTimeProperty.set(selectedTourLog.getTotalTime());
        ratingProperty.set(selectedTourLog.getRating());
    }

    private void bindProperties() {
        dateTextField.textProperty().bindBidirectional(dateProperty);
        commentTextField.textProperty().bindBidirectional(commentProperty);
        difficultyTextField.textProperty().bindBidirectional(difficultyProperty);
        totalDistanceTextField.textProperty().bindBidirectional(totalDistanceProperty);
        totalTimeTextField.textProperty().bindBidirectional(totalTimeProperty);
        ratingTextField.textProperty().bindBidirectional(ratingProperty);
    }

    @FXML
    private void saveTourLog() {
        selectedTourLog.setDate(dateProperty.get());
        selectedTourLog.setComment(commentProperty.get());
        selectedTourLog.setDifficulty(difficultyProperty.get());
        selectedTourLog.setTotalDistance(totalDistanceProperty.get());
        selectedTourLog.setTotalTime(totalTimeProperty.get());
        selectedTourLog.setRating(ratingProperty.get());

        tourLogViewModel.updateTourLog(selectedTourLog);

        stage.close();
    }
}
