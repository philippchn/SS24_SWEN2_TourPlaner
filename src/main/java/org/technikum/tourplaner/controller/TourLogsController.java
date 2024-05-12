package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import java.util.List;
import java.util.Map;

public class TourLogsController {
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

    public TourLogsController(TourViewModel tourViewModel)
    {
        this.tourViewModel = tourViewModel;
    }

    private TourLogViewModel tourLogViewModel = new TourLogViewModel();

    public TourLogsController(TourViewModel tourViewModel, TourLogViewModel tourLogViewModel) {
        this.tourViewModel = tourViewModel;
        this.tourLogViewModel = tourLogViewModel;
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
            }
        });
    }

    @FXML
    private void addTourLog() {
        System.out.println("Adding new tour log...");
        TourModel selectedTour = tourViewModel.selectedTourModelProperty().get();
        if(selectedTour != null) {
            String date = dateTextField.getText();
            String comment = commentTextField.getText();
            String difficulty = difficultyTextField.getText();
            String totalDistance = totalDistanceTextField.getText();
            String totalTime = totalTimeTextField.getText();
            String rating = ratingTextField.getText();

            // Create a new TourLogModel
            TourLogModel newTourLogModel = new TourLogModel(date, comment, difficulty, totalDistance, totalTime, rating);

            // Add the new tour log to the tour model and view model
            selectedTour.addTourLog(String.valueOf(selectedTourProperty), newTourLogModel);
            tourLogViewModel.getTourLogModelList().add(newTourLogModel);

            // Clear the text fields
            clearTextFields();
            // Print the updated tour logs map and repopulate the logs table
            System.out.println(selectedTour.getTourLogsMap());
            //setupColumns();
            logsTable.getItems().clear();
            populateLogsTable(selectedTour);
        }
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
        // Set cell value factories for each column to display data from TourLogModel
        TableColumn<TourLogModel, String> dateColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(0);
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());

        TableColumn<TourLogModel, String> durationColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(1);
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalTime());

        TableColumn<TourLogModel, String> distanceColumn = (TableColumn<TourLogModel, String>) logsTable.getColumns().get(2);
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalDistance());
    }
}
