package org.technikum.tourplaner.viewmodels;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.controller.ModifyTourPopupController;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.repositories.TourRepository;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TourViewModel {
    private final TourRepository tourRepository;

    @Getter
    private ObservableList<TourModel> tours = FXCollections.observableArrayList();
    private final ObjectProperty<TourModel> selectedTourModelProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLogModel> selectedTourLogModelProperty = new SimpleObjectProperty<>();

    private final SimpleStringProperty nameProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleStringProperty fromProperty = new SimpleStringProperty();
    private final SimpleStringProperty toProperty = new SimpleStringProperty();
    private final SimpleStringProperty transportTypeProperty = new SimpleStringProperty();
    private final SimpleStringProperty statusMessageProperty = new SimpleStringProperty();
    private final SimpleStringProperty detailViewNameProperty = new SimpleStringProperty("Name:");
    private final SimpleStringProperty detailViewDescriptionProperty = new SimpleStringProperty("Description:");
    private final SimpleStringProperty detailViewFromProperty = new SimpleStringProperty("From:");
    private final SimpleStringProperty detailViewToProperty = new SimpleStringProperty("To:");
    private final SimpleStringProperty detailViewTransportTypeProperty = new SimpleStringProperty("Transport type:");
    private final ObjectProperty<Image> detailViewMapImageProperty = new SimpleObjectProperty<>();

    public ObjectProperty<TourModel> selectedTourModelProperty() {
        return selectedTourModelProperty;
    }

    public ObjectProperty<TourLogModel> selectedTourLogModelProperty() {
        return selectedTourLogModelProperty;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    public StringProperty fromProperty() {
        return fromProperty;
    }

    public StringProperty toProperty() {
        return toProperty;
    }

    public StringProperty statusMessageProperty() {
        return statusMessageProperty;
    }

    public StringProperty transportTypeProperty() {
        return transportTypeProperty;
    }

    public StringProperty detailViewNameProperty() {
        return detailViewNameProperty;
    }

    public StringProperty detailViewDescriptionProperty() {
        return detailViewDescriptionProperty;
    }

    public StringProperty detailViewFromProperty() {
        return detailViewFromProperty;
    }

    public StringProperty detailViewToProperty() {
        return detailViewToProperty;
    }

    public StringProperty detailViewTransportTypeProperty() {
        return detailViewTransportTypeProperty;
    }

    public ObjectProperty<Image> detailViewMapImageProperty() {
        return detailViewMapImageProperty;
    }

    public TourModel getSelectedTourModel() {
        return selectedTourModelProperty.get();
    }

    public TourLogModel getSelectedTourLogModel() {
        return selectedTourLogModelProperty.get();
    }

    public TourViewModel(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
        loadTours();
    }

    private void loadTours() {
        tours.clear(); // Clear existing tours

        List<TourModel> tourList = tourRepository.getAllTours();
        tours.addAll(tourList);
    }


    public void addTour() {
        if (isValidInput()) {
            TourModel newTour = new TourModel(
                    nameProperty.get().trim(),
                    descriptionProperty.get().trim(),
                    fromProperty.get().trim(),
                    toProperty.get().trim(),
                    transportTypeProperty.get().trim()
            );

            tours.add(newTour);
            tourRepository.save(newTour);
            clearInputFields();
        }
    }

    private boolean isValidInput() {
        if (nameProperty.get() == null || nameProperty.get().isBlank()) {
            showErrorMessage("Name");
            return false;
        } else if (fromProperty.get() == null || fromProperty.get().isBlank()) {
            showErrorMessage("From");
            return false;
        } else if (toProperty.get() == null || toProperty.get().isBlank()) {
            showErrorMessage("To");
            return false;
        } else if (transportTypeProperty.get() == null || transportTypeProperty.get().isBlank()) {
            showErrorMessage("Transport type");
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-ZäÄöÖüÜ]+$");
        Matcher validateFrom = pattern.matcher(fromProperty.get().trim());
        Matcher validateTo = pattern.matcher(toProperty.get().trim());

        if (!(validateFrom.matches() && validateTo.matches())) {
            statusMessageProperty().set("From and To must not contain numbers or special characters");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String missingTextField) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(missingTextField + " must not be empty");
        alert.showAndWait();
    }

    private void clearInputFields() {
        nameProperty.set("");
        descriptionProperty.set("");
        fromProperty.set("");
        toProperty.set("");
        transportTypeProperty.set("");
        statusMessageProperty.set("Create new tour:");
    }

    public void setCurrentlyClickedTour(TourModel tourModel) {
        selectedTourModelProperty.set(tourModel);
        if (selectedTourModelProperty.get() == null) {
            return;
        }
        setDetailView(selectedTourModelProperty.get());
    }

    private void setDetailView(TourModel selectedItem) {
        if (selectedItem == null) {
            return;
        }
        detailViewNameProperty.set("Name: " + selectedItem.getName());
        detailViewDescriptionProperty.set("Description: " + selectedItem.getTourDescription());
        detailViewFromProperty.set("From: " + selectedItem.getFrom());
        detailViewToProperty.set("To: " + selectedItem.getTo());
        detailViewTransportTypeProperty.set("Transport type: " + selectedItem.getTransportType());
        detailViewMapImageProperty.set(new Image(getClass().getResource("/org/technikum/tourplaner/img/mapPlaceholder.jpg").toExternalForm()));
    }

    public void deleteTour() {
        TourModel selectedTour = selectedTourModelProperty.get();
        if (selectedTour != null) {
            tourRepository.deleteById(selectedTour.getId());
            tours.remove(selectedTour);
        }
    }

    public void updateTour() {
        TourModel selectedTour = selectedTourModelProperty.get();
        if (selectedTour != null) {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(EViews.modifyTourPopup.getFilePath()));
                Parent root = loader.load();

                ModifyTourPopupController controller = loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                controller.initData(selectedTour, stage, this);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();

                // After modifying, update the tour in the repository
                tourRepository.updateById(selectedTour.getId(), selectedTour);

                updateDisplayedTourList(selectedTour);
                setDetailView(selectedTour);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDisplayedTourList(TourModel selectedTour) {
        for (int i = 0; i < tours.size(); i++) {
            if (tours.get(i).getId().equals(selectedTour.getId())) {
                tours.set(i, selectedTour);
                break;
            }
        }
    }
}
