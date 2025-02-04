package org.technikum.tourplaner.BL.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.BL.viewmodels.TourViewModel;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;

public class TourLogsController {
    @FXML
    private Button reportPdfButton;
    @FXML
    private Button summarizePdfButton;
    @FXML
    private Button leafletButton;
    @FXML
    private Button importButton;
    @FXML
    private Button exportButton;
    @FXML
    private Text infoText;
    @FXML
    private TableView<TourLogModel> logsTable;
    @FXML
    private Button saveButton;
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

    public TourLogsController(TourRepository tourRepository, TourViewModel tourViewModel, TourLogRepository tourLogRepository) {
        this.tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModel);
    }

    @FXML
    private void initialize() {
        bindProperties();

        saveButton.setOnAction(event -> tourLogViewModel.addTourLog());
        logsTable.setOnMouseClicked(event -> tourLogViewModel.selectTourLog());
        deleteButton.setOnAction(event -> tourLogViewModel.deleteTourLog());
        modifyButton.setOnAction(event -> tourLogViewModel.openModifyTourLogPopup(logsTable));
        leafletButton.setOnAction(event -> tourLogViewModel.openLeafletMap());
        reportPdfButton.setOnAction(event -> tourLogViewModel.generateTourReportPdf());
        summarizePdfButton.setOnAction(event ->tourLogViewModel.generateSummarizePdf());
        importButton.setOnAction(event -> tourLogViewModel.importTour());
        exportButton.setOnAction(actionEvent -> tourLogViewModel.exportTour());

        logsTable.setItems(tourLogViewModel.getTourLogs());
        tourLogViewModel.initializeLogsTableColumns(logsTable);
    }

    private void bindProperties() {
        datePicker.valueProperty().bindBidirectional(tourLogViewModel.dateProperty());
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
