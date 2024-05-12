package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import java.util.List;
import java.util.Map;

public class TourLogsController {
    @FXML
    private Text infoText;
    @FXML
    private Label currentlySelectedTourLabel;
    @FXML
    private TableView<TourLogModel> logsTable;
    @FXML
    private Button saveButton;

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

    private final SimpleStringProperty dateProperty = new SimpleStringProperty();
    private final SimpleStringProperty commentProperty = new SimpleStringProperty();
    private final SimpleStringProperty difficultyProperty = new SimpleStringProperty();
    private final SimpleStringProperty totalDistanceProperty = new SimpleStringProperty();
    private final SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private final SimpleStringProperty ratingProperty = new SimpleStringProperty();
    private final SimpleStringProperty selectedTourProperty = new SimpleStringProperty();

    private final TourViewModel tourViewModel;
    private TourLogViewModel tourLogViewModel;

    public TourLogsController(TourViewModel tourViewModel)
    {
        this.tourViewModel = tourViewModel;
        this.tourLogViewModel = new TourLogViewModel();
    }

    @FXML
    private void initialize() {
        bindProperties();
        addListener();
        setupColumns();
        saveButton.setOnAction(event -> addTourLog());
    }

    private void bindProperties() {
        currentlySelectedTourLabel.textProperty().bindBidirectional(selectedTourProperty);
        dateTextField.textProperty().bindBidirectional(dateProperty);
        commentTextField.textProperty().bindBidirectional(commentProperty);
        difficultyTextField.textProperty().bindBidirectional(difficultyProperty);
        totalDistanceTextField.textProperty().bindBidirectional(totalDistanceProperty);
        totalTimeTextField.textProperty().bindBidirectional(totalTimeProperty);
        ratingTextField.textProperty().bindBidirectional(ratingProperty);
    }

    private void addListener() {
        tourViewModel.selectedTourModelProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends TourModel> observableValue, TourModel oldValue, TourModel newValue)
            {
                System.out.println("Changed");
                selectedTourProperty.set(newValue.toString());
                logsTable.getItems().clear();
                TourModel selectedTour = tourViewModel.selectedTourModelProperty().get();
                populateLogsTable(selectedTour);
            }
        });
    }

    @FXML
    private void addTourLog() {
        System.out.println("Adding new tour log...");
        TourModel selectedTour = tourViewModel.selectedTourModelProperty().get();
        if(selectedTour != null && isValidInput()) {
            TourLogModel tourLogModel = getTourLogModelFromGUI();

            selectedTour.addTourLog(String.valueOf(selectedTourProperty), tourLogModel);
            tourLogViewModel.getTourLogModelList().add(tourLogModel);

            clearTextFields();

            logsTable.getItems().clear();
            populateLogsTable(selectedTour);
        }
    }

    private TourLogModel getTourLogModelFromGUI()
    {
        String date = dateTextField.getText();
        String comment = commentTextField.getText();
        String difficulty = difficultyTextField.getText();
        String totalDistance = totalDistanceTextField.getText();
        String totalTime = totalTimeTextField.getText();
        String rating = ratingTextField.getText();

        return new TourLogModel(date, comment, difficulty, totalDistance, totalTime, rating);
    }

    private boolean isValidInput() {
        if (dateTextField.getText() == null || dateTextField.getText().isBlank()) {
            setErrorMessage(dateTextField);
            return false;
        } else if (commentTextField.getText() == null || commentTextField.getText().isBlank()) {
            setErrorMessage(commentTextField);
            return false;
        } else if (difficultyTextField.getText() == null || difficultyTextField.getText().isBlank()) {
            setErrorMessage(difficultyTextField);
            return false;
        } else if (totalDistanceTextField.getText() == null || totalDistanceTextField.getText().isBlank()) {
            setErrorMessage(totalDistanceTextField);
            return false;
        }
        else if (totalTimeTextField.getText() == null || totalTimeTextField.getText().isBlank()) {
            setErrorMessage(totalTimeTextField);
            return false;
        } else if (ratingTextField.getText() == null || ratingTextField.getText().isBlank()) {
            setErrorMessage(ratingTextField);
            return false;
        }

        infoText.setFill(Color.BLACK);
        return true;
    }

    private void setErrorMessage(TextField missingTextField)
    {
        infoText.setFill(Color.RED);
        infoText.setText(missingTextField.getPromptText() + " must not be empty");
    }

    private void clearTextFields() {
        dateTextField.clear();
        commentTextField.clear();
        difficultyTextField.clear();
        totalDistanceTextField.clear();
        totalTimeTextField.clear();
        ratingTextField.clear();
    }

    private void populateLogsTable(TourModel selectedTour) {
        if (selectedTour != null) {
            Map<String, List<TourLogModel>> tourLogsMap = selectedTour.getTourLogsMap();
            tourLogsMap.forEach((key, value) -> {
                for (TourLogModel logModel : value) {
                    System.out.println("Date: " + logModel.getDate().getValue());
                    System.out.println("Duration: " + logModel.getTotalTime().getValue());
                    System.out.println("Distance: " + logModel.getTotalDistance().getValue());
                }
                logsTable.getItems().addAll(value);
            });
        }
    }

    private void setupColumns() {
        TableColumn<TourLogModel, String> dateColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(0);
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());

        TableColumn<TourLogModel, String> durationColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(1);
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalTime());

        TableColumn<TourLogModel, String> distanceColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(2);
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalDistance());
    }

}
