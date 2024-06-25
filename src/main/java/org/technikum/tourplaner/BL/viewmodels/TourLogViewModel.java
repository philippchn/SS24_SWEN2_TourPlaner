package org.technikum.tourplaner.BL.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.controller.ModifyTourLogsPopupController;
import org.technikum.tourplaner.BL.iText7.PdfGenerator;
import org.technikum.tourplaner.BL.models.ExportUtil;
import org.technikum.tourplaner.BL.models.ImportUtil;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.models.TourModel;
import org.technikum.tourplaner.DAL.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class TourLogViewModel {
    private static final Logger logger = LogManager.getLogger(TourLogViewModel.class);

    private final TourRepository tourRepository;
    private final TourLogRepository tourLogRepository;
    private final TourViewModel tourViewModel;

    private final ObservableList<TourLogModel> tourLogModelList = FXCollections.observableArrayList();
    private final ObjectProperty<TourLogModel> selectedTourLogModelProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>();
    private final SimpleStringProperty commentProperty = new SimpleStringProperty();
    private final SimpleStringProperty difficultyProperty = new SimpleStringProperty();
    private final SimpleStringProperty totalDistanceProperty = new SimpleStringProperty();
    private final SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private final SimpleStringProperty ratingProperty = new SimpleStringProperty();

    private final SimpleStringProperty detailViewDateProperty = new SimpleStringProperty("Date: ");
    private final SimpleStringProperty detailViewCommentProperty = new SimpleStringProperty("Comment: ");
    private final SimpleStringProperty detailViewDifficultyProperty = new SimpleStringProperty("Difficulty: ");
    private final SimpleStringProperty detailViewTotalDistanceProperty = new SimpleStringProperty("Total distance: ");
    private final SimpleStringProperty detailViewTotalTimeProperty = new SimpleStringProperty("Total time: ");
    private final SimpleStringProperty detailViewRatingProperty = new SimpleStringProperty("Rating: ");

    public TourLogViewModel(TourRepository tourRepository, TourLogRepository tourLogRepository, TourViewModel tourViewModel) {
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogRepository;
        this.tourViewModel = tourViewModel;
        tourViewModel.selectedTourModelProperty().addListener((observable, oldValue, newValue) -> loadTourLogs(newValue));
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return dateProperty;
    }

    public StringProperty commentProperty() {
        return commentProperty;
    }

    public StringProperty difficultyProperty() {
        return difficultyProperty;
    }

    public StringProperty totalDistanceProperty() {
        return totalDistanceProperty;
    }

    public StringProperty totalTimeProperty() {
        return totalTimeProperty;
    }

    public StringProperty ratingProperty() {
        return ratingProperty;
    }

    public StringProperty detailViewDateProperty() {
        return detailViewDateProperty;
    }

    public StringProperty detailViewCommentProperty() {
        return detailViewCommentProperty;
    }

    public StringProperty detailViewDifficultyProperty() {
        return detailViewDifficultyProperty;
    }

    public StringProperty detailViewTotalDistanceProperty() {
        return detailViewTotalDistanceProperty;
    }

    public StringProperty detailViewTotalTimeProperty() {
        return detailViewTotalTimeProperty;
    }

    public StringProperty detailViewRatingProperty() {
        return detailViewRatingProperty;
    }

    public void setSelectedTourLogModel(TourLogModel tourLogModel) {
        selectedTourLogModelProperty.set(tourLogModel);
    }

    public void initializeLogsTableColumns(TableView<TourLogModel> logsTable) {
        TableColumn<TourLogModel, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));

        TableColumn<TourLogModel, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTotalTime().toString()));

        TableColumn<TourLogModel, String> distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTotalDistance().toString()));

        logsTable.getColumns().setAll(dateColumn, durationColumn, distanceColumn);
    }

    public ObservableList<TourLogModel> getTourLogs() {
        return tourLogModelList;
    }

    private void loadTourLogs(TourModel selectedTour) {
        if (selectedTour != null) {
            tourLogModelList.clear();
            tourLogModelList.addAll(tourLogRepository.getTourLogsByTourId(String.valueOf(selectedTour.getId())));
        }
    }

    private List<TourLogModel> getTourLogs(TourModel selectedTour) {
        Map<String, List<TourLogModel>> tourLogsMap = selectedTour.getTourLogsMap();
        return tourLogsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public void addTourLog() {
        try {
            if (!isValidInput()) {
                return;
            }

            TourLogModel tourLogModel = new TourLogModel(
                    dateProperty.get(),
                    commentProperty.get(),
                    Integer.parseInt(difficultyProperty.get()),
                    Double.parseDouble(totalDistanceProperty.get()),
                    Long.parseLong(totalTimeProperty.get()),
                    Integer.parseInt(ratingProperty.get()),
                    tourViewModel.getSelectedTourModel().getId()
            );

            TourModel selectedTour = tourViewModel.getSelectedTourModel();

            if (selectedTour == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please create/select a tour first");
                alert.showAndWait();
                return;
            }

            selectedTour.addTourLog(selectedTour.getName(), tourLogModel);
            tourLogModelList.add(tourLogModel);
            tourLogRepository.save(tourLogModel);
            clearTextFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for Difficulty, Total Distance, Total Time, and Rating.");
        }
    }

    public void selectTourLog() {
        if (selectedTourLogModelProperty.get() == null) {
            return;
        }
        updateDetailView(selectedTourLogModelProperty.get());
    }

    public void deleteTourLog() {
        TourLogModel selectedTourLog = selectedTourLogModelProperty.get();
        if (selectedTourLog != null) {
            tourLogRepository.deleteById(selectedTourLog.getId());
            tourLogModelList.remove(selectedTourLog);
            clearDetailView();
        } else {
            showAlert("No tour log selected", "Please select a tour log to delete.");
        }
    }

    public void updateTourLog(TourLogModel updatedTourLog) {
        tourLogRepository.updateById(updatedTourLog.getId(), updatedTourLog);
        tourLogModelList.replaceAll(log -> log.getId().equals(updatedTourLog.getId()) ? updatedTourLog : log);
    }

    public void openModifyTourLogPopup(TableView<TourLogModel> logsTable) {
        TourLogModel selectedTourLog = selectedTourLogModelProperty.get();
        if (selectedTourLog != null) {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(EViews.modifyTourLogPopup.getFilePath()));
                Parent root = loader.load();

                ModifyTourLogsPopupController controller = loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);

                controller.initData(selectedTourLog, stage, this);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();

                tourLogRepository.updateById(selectedTourLog.getId(), selectedTourLog);

                logsTable.refresh();

            } catch (IOException e) {
                logger.fatal("Error while opening modify tourLog popup");
            }
        } else {
            showAlert("No tour log selected", "Please select a tour log to modify.");
        }
    }

    public boolean isValidInput() {
        try {
            if (dateProperty.get() == null) {
                throw new IllegalArgumentException("Date must not be null");
            }
            if (commentProperty.get() == null || commentProperty.get().isBlank()) {
                throw new IllegalArgumentException("Comment must not be empty");
            }
            int difficulty;
            try {
                difficulty = Integer.parseInt(difficultyProperty.get());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Difficulty must be a valid integer");
            }
            if (difficulty <= 0 || difficulty > 10) {
                throw new IllegalArgumentException("Difficulty must be between 1 and 10");
            }
            double totalDistance;
            try {
                totalDistance = Double.parseDouble(totalDistanceProperty.get());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Total distance must be a valid number");
            }
            if (totalDistance < 0) {
                throw new IllegalArgumentException("Total distance must be a non-negative number");
            }
            long totalTime;
            try {
                totalTime = Long.parseLong(totalTimeProperty.get());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Total time must be a valid number");
            }
            if (totalTime < 0) {
                throw new IllegalArgumentException("Total time must be a non-negative number");
            }
            int rating;
            try {
                rating = Integer.parseInt(ratingProperty.get());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Rating must be a valid integer");
            }
            if (rating < 0) {
                throw new IllegalArgumentException("Rating must be a non-negative number");
            }
            return true;
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
            return false;
        }
    }

    private void showErrorMessage(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateDetailView(TourLogModel selectedTourLogModel) {
        detailViewDateProperty.set("Date: " + selectedTourLogModel.getDate());
        detailViewCommentProperty.set("Comment: " + selectedTourLogModel.getComment());
        detailViewDifficultyProperty.set("Difficulty: " + selectedTourLogModel.getDifficulty());
        detailViewTotalDistanceProperty.set("Total distance: " + selectedTourLogModel.getTotalDistance());
        detailViewTotalTimeProperty.set("Total time: " + selectedTourLogModel.getTotalTime());
        detailViewRatingProperty.set("Rating: " + selectedTourLogModel.getRating());
    }

    private void clearDetailView() {
        detailViewDateProperty.set("Date: ");
        detailViewCommentProperty.set("Comment: ");
        detailViewDifficultyProperty.set("Difficulty: ");
        detailViewTotalDistanceProperty.set("Total distance: ");
        detailViewTotalTimeProperty.set("Total time: ");
        detailViewRatingProperty.set("Rating: ");
    }

    private void clearTextFields() {
        dateProperty.set(null);
        commentProperty.set("");
        difficultyProperty.set("");
        totalDistanceProperty.set("");
        totalTimeProperty.set("");
        ratingProperty.set("");
    }

    public void openLeafletMap() {
        if (tourViewModel.selectedTourModelProperty().get() == null) {
            logger.info("User tried to open a Leaflet map without selecting a Tour");
            return;
        }
        OpenRouteServiceClient.openTourMapInBrowser(tourViewModel.selectedTourModelProperty().get().getRouteInformation());
    }

    public void generatePdf() {
        if (tourViewModel.selectedTourModelProperty().get() == null) {
            logger.info("User tried to generade pdf without selecting a Tour");
            return;
        }
        try{
            PdfGenerator.generatePdf(tourViewModel.selectedTourModelProperty().get(), tourLogModelList);
        } catch (FileNotFoundException e) {
            logger.warn("PDF Generation failed");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("PDF Generation failed");
            alert.showAndWait();
        }
    }

    public void importTour() {
        ImportUtil importUtil = new ImportUtil(tourRepository, tourLogRepository);
        importUtil.importTourData();
    }

    public void exportTour() {
        ExportUtil exportUtil = new ExportUtil();
        exportUtil.exportTourData(tourViewModel.selectedTourModelProperty().get(), tourLogModelList);
    }
}
