package org.technikum.tourplaner.viewmodels;

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
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.controller.ModifyTourLogsPopupController;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class TourLogViewModel {
    private final TourViewModel tourViewModel;
    private final ObservableList<TourLogModel> tourLogModelList = FXCollections.observableArrayList();
    private final ObjectProperty<TourModel> selectedTourModelProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLogModel> selectedTourLogModelProperty = new SimpleObjectProperty<>();

    private final SimpleStringProperty dateProperty = new SimpleStringProperty();
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

    public TourLogViewModel(TourViewModel tourViewModel) {
        this.tourViewModel = tourViewModel;
        tourViewModel.selectedTourModelProperty().addListener((observable, oldValue, newValue) -> loadTourLogs(newValue));
    }

    public StringProperty dateProperty() {
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
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());

        TableColumn<TourLogModel, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalTime());

        TableColumn<TourLogModel, String> distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTotalDistance());

        logsTable.getColumns().setAll(dateColumn, durationColumn, distanceColumn);
    }

    public ObservableList<TourLogModel> getTourLogs() {
        return tourLogModelList;
    }

    private void loadTourLogs(TourModel selectedTour) {
        if (selectedTour != null) {
            tourLogModelList.clear();
            tourLogModelList.addAll(getTourLogs(selectedTour));
        }
    }

    private List<TourLogModel> getTourLogs(TourModel selectedTour) {
        Map<String, List<TourLogModel>> tourLogsMap = selectedTour.getTourLogsMap();
        return tourLogsMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public void addTourLog() {
        if (selectedTourLogModelProperty.get() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please create/select a tour first");
            alert.showAndWait();
            return;
        }

        if (!isValidInput()) {
            return;
        }

        TourLogModel tourLogModel = createTourLogModel();
        TourModel selectedTour = tourViewModel.getSelectedTourModel();
        selectedTour.addTourLog(selectedTour.getName().get(), tourLogModel);
        tourLogModelList.add(tourLogModel);
        clearTextFields();
    }

    public void selectTourLog() {
        if (selectedTourLogModelProperty.get() == null) {
            return;
        }
        updateDetailView(selectedTourLogModelProperty.get());
    }

    public void updateTourLog(TourLogModel updatedTourLog) {
        for (int i = 0; i < tourLogModelList.size(); i++) {
            TourLogModel tourLog = tourLogModelList.get(i);
            if (tourLog.getComment().equals(updatedTourLog.getComment())) { // used comment here for some unique feature but we will use id when we implement the db
                tourLogModelList.set(i, updatedTourLog);
                break;
            }
        }
    }

    public void deleteTourLog() {
        TourLogModel selectedTourLog = selectedTourLogModelProperty.get();
        if (selectedTourLog != null) {
            tourLogModelList.remove(selectedTourLog);
            clearDetailView();
        }

        /*
        System.out.println("\nTour logs list after deletion:");
        tourLogModelList.forEach(log -> {
            System.out.println("Date: " + log.getDate().get());
            System.out.println("Comment: " + log.getComment().get());
            System.out.println("Difficulty: " + log.getDifficulty().get());
            System.out.println("Total Distance: " + log.getTotalDistance().get());
            System.out.println("Total Time: " + log.getTotalTime().get());
            System.out.println("Rating: " + log.getRating().get() + "\n");
        });*/
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
                logsTable.refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValidInput() {
        if (dateProperty.get() == null || dateProperty.get().isBlank()) {
            showErrorMessage("Date");
            return false;
        } else if (commentProperty.get() == null || commentProperty.get().isBlank()) {
            showErrorMessage("Comment");
            return false;
        } else if(difficultyProperty.get() == null || difficultyProperty.get().isBlank()) {
            showErrorMessage("Difficulty");
            return false;
        } else if(totalDistanceProperty.get() == null || totalDistanceProperty.get().isBlank()) {
            showErrorMessage("Total distance");
            return false;
        } else if(totalTimeProperty.get() == null || totalTimeProperty.get().isBlank()) {
            showErrorMessage("Total time");
            return false;
        } else if(ratingProperty.get() == null || ratingProperty.get().isBlank()) {
            showErrorMessage("Rating");
            return false;
        }
        return true;
    }

    private TourLogModel createTourLogModel() {
        return new TourLogModel(dateProperty.get(), commentProperty.get(), difficultyProperty.get(),
                totalDistanceProperty.get(), totalTimeProperty.get(), ratingProperty.get());
    }

    private void showErrorMessage(String missingTextField) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(missingTextField + " must not be empty");
        alert.showAndWait();
    }

    private void updateDetailView(TourLogModel selectedTourLogModel) {
        detailViewDateProperty.set("Date: " + selectedTourLogModel.getDate().get());
        detailViewCommentProperty.set("Comment: " + selectedTourLogModel.getComment().get());
        detailViewDifficultyProperty.set("Difficulty: " + selectedTourLogModel.getDifficulty().get());
        detailViewTotalDistanceProperty.set("Total distance: " + selectedTourLogModel.getTotalDistance().get());
        detailViewTotalTimeProperty.set("Total time: " + selectedTourLogModel.getTotalTime().get());
        detailViewRatingProperty.set("Rating: " + selectedTourLogModel.getRating().get());
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
        dateProperty.set("");
        commentProperty.set("");
        difficultyProperty.set("");
        totalDistanceProperty.set("");
        totalTimeProperty.set("");
        ratingProperty.set("");
    }
}
