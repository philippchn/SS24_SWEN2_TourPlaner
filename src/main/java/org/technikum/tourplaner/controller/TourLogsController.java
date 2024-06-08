package org.technikum.tourplaner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.repositories.TourLogRepository;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

public class TourLogsController {
    @FXML
    private Text infoText;
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
    @FXML
    private Text detailViewDate;
    @FXML
    private Text detailViewComment;
    @FXML
    private Text detailViewDifficulty;
    @FXML
    private Text detailViewTotalDistance;
    @FXML
    private Text detailViewTotalTime;
    @FXML
    private Text detailViewRating;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;

    private final TourLogViewModel tourLogViewModel;

    public TourLogsController(TourViewModel tourViewModel, TourLogRepository tourLogRepository) {
        this.tourLogViewModel = new TourLogViewModel(tourLogRepository, tourViewModel);
    }

    @FXML
    private void initialize() {
        bindProperties();

        saveButton.setOnAction(event -> tourLogViewModel.addTourLog());
        logsTable.setOnMouseClicked(event -> tourLogViewModel.selectTourLog());
        deleteButton.setOnAction(event -> tourLogViewModel.deleteTourLog());
        modifyButton.setOnAction(event -> tourLogViewModel.openModifyTourLogPopup(logsTable));

        logsTable.setItems(tourLogViewModel.getTourLogs());
        tourLogViewModel.initializeLogsTableColumns(logsTable);
    }

    private void bindProperties() {
        dateTextField.textProperty().bindBidirectional(tourLogViewModel.dateProperty());
        commentTextField.textProperty().bindBidirectional(tourLogViewModel.commentProperty());
        difficultyTextField.textProperty().bindBidirectional(tourLogViewModel.difficultyProperty());
        totalDistanceTextField.textProperty().bindBidirectional(tourLogViewModel.totalDistanceProperty());
        totalTimeTextField.textProperty().bindBidirectional(tourLogViewModel.totalTimeProperty());
        ratingTextField.textProperty().bindBidirectional(tourLogViewModel.ratingProperty());

        detailViewDate.textProperty().bind(tourLogViewModel.detailViewDateProperty());
        detailViewComment.textProperty().bind(tourLogViewModel.detailViewCommentProperty());
        detailViewDifficulty.textProperty().bind(tourLogViewModel.detailViewDifficultyProperty());
        detailViewTotalDistance.textProperty().bind(tourLogViewModel.detailViewTotalDistanceProperty());
        detailViewTotalTime.textProperty().bind(tourLogViewModel.detailViewTotalTimeProperty());
        detailViewRating.textProperty().bind(tourLogViewModel.detailViewRatingProperty());

        tourLogViewModel.getSelectedTourLogModelProperty().bind(logsTable.getSelectionModel().selectedItemProperty());
    }
}
