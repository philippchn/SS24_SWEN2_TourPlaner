package org.technikum.tourplaner.viewmodels;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.controller.ModifyTourPopupController;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.openrouteservice.ETransportType;
import org.technikum.tourplaner.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.repositories.TourRepository;

import java.io.IOException;
import java.util.List;

public class TourViewModel {
    private static final Logger logger = LogManager.getLogger(TourViewModel.class);

    private final TourRepository tourRepository;
    private final OpenRouteServiceClient openRouteServiceClient;

    @Getter
    private ObservableList<TourModel> tours = FXCollections.observableArrayList();
    private final ObjectProperty<TourModel> selectedTourModelProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLogModel> selectedTourLogModelProperty = new SimpleObjectProperty<>();

    private final SimpleStringProperty nameProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleStringProperty fromProperty = new SimpleStringProperty();
    private final SimpleStringProperty toProperty = new SimpleStringProperty();
    private final ObjectProperty<ETransportType> transportTypeProperty = new SimpleObjectProperty<ETransportType>();
    private final SimpleStringProperty statusMessageProperty = new SimpleStringProperty();
    private final SimpleStringProperty detailViewNameProperty = new SimpleStringProperty("Name:");
    private final SimpleStringProperty detailViewDescriptionProperty = new SimpleStringProperty("Description:");
    private final SimpleStringProperty detailViewFromProperty = new SimpleStringProperty("From:");
    private final SimpleStringProperty detailViewToProperty = new SimpleStringProperty("To:");
    private final SimpleStringProperty detailViewDistanceProperty = new SimpleStringProperty("Distance:");
    private final SimpleStringProperty detailViewEstimatedTimeProperty = new SimpleStringProperty("Estimated time:");
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

    public StringProperty detailViewDistanceProperty() {
        return detailViewDistanceProperty;
    }

    public StringProperty detailViewEstimatedTimeProperty() {
        return detailViewEstimatedTimeProperty;
    }

    public StringProperty statusMessageProperty() {
        return statusMessageProperty;
    }

    public ObjectProperty<ETransportType> transportTypeProperty() {
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

    public void setTours(ObservableList<TourModel> toursList) {
        tours.setAll(toursList);
    }

    public TourViewModel(TourRepository tourRepository, OpenRouteServiceClient openRouteServiceClient) {
        this.tourRepository = tourRepository;
        this.openRouteServiceClient = openRouteServiceClient;
        loadToursFromDatabase();
    }

    public void loadToursFromDatabase() {
        tours.clear();
        List<TourModel> tourList = tourRepository.getAllTours();
        tours.addAll(tourList);
    }

    public void addTour() {
        if (!allFieldsSet()) {
            return;
        }

        try {
            String name = nameProperty.get();
            String description = descriptionProperty.get();
            String from = fromProperty.get();
            String to = toProperty.get();
            ETransportType transportType = transportTypeProperty.get();

            String routeInformation = openRouteServiceClient.getTourInformation(from, to, transportType);
            TourModel newTour = parseRouteResponse(routeInformation, name, description, from, to, transportType);

            tours.add(newTour);
            tourRepository.save(newTour);
            clearInputFields();
        } catch (IllegalArgumentException e) {
            showErrorMessage("Failed to add tour. Check log file for details");
        }
    }

    public boolean allFieldsSet() {
        if (nameProperty.get() == null || nameProperty.get().isBlank()) {
            showErrorMessage("Name must not be empty");
            logger.info("Input field 'name' is empty");
            return false;
        } else if (descriptionProperty.get() == null || descriptionProperty.get().isBlank()) {
            showErrorMessage("Description must not be empty");
            logger.info("Input field 'Description' was empty");
            return false;
        } else if (fromProperty.get() == null || fromProperty.get().isBlank()) {
            showErrorMessage("From must not be empty");
            logger.info("Input field 'From' was empty");
            return false;
        } else if (toProperty.get() == null || toProperty.get().isBlank()) {
            showErrorMessage("To must not be empty");
            logger.info("Input field 'To' was empty");
            return false;
        } else if (transportTypeProperty.get() == null) {
            showErrorMessage("Transport type must not be empty");
            logger.info("Input field 'Transport type' was empty");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private void clearInputFields() {
        nameProperty.set("");
        descriptionProperty.set("");
        fromProperty.set("");
        toProperty.set("");
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
        detailViewDistanceProperty.set("Distance: " + selectedItem.getDistance());
        detailViewEstimatedTimeProperty.set("Estimated Time: " + selectedItem.getEstimatedTime());
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
                ModifyTourPopupController controller = new ModifyTourPopupController(this);
                loader.setController(controller);

                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);

                controller.initData(selectedTour, stage);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();

                String updatedName = nameProperty.get();
                String updatedDescription = descriptionProperty.get();
                String updatedFrom = fromProperty.get();
                String updatedTo = toProperty.get();

                ETransportType updatedTransportType = transportTypeProperty.get();

                String routeInformation = openRouteServiceClient.getTourInformation(updatedFrom, updatedTo, updatedTransportType);
                TourModel updatedTour = parseRouteResponse(routeInformation, updatedName, updatedDescription, updatedFrom, updatedTo, updatedTransportType);

                tourRepository.updateById(selectedTour.getId(), updatedTour);

                updateDisplayedTourList(updatedTour);

                setDetailView(updatedTour);
                clearInputFields();
            } catch (IOException | IllegalArgumentException e) {
                logger.warn("Error updating tour: " + e.getMessage());
            }
        }
    }

    private void updateDisplayedTourList(TourModel updatedTour) {
        for (int i = 0; i < tours.size(); i++) {
            if (tours.get(i).getId().equals(updatedTour.getId())) {
                tours.set(i, updatedTour);
                break;
            }
        }
    }

    public TourModel parseRouteResponse(String jsonResponse, String name, String description, String from, String to, ETransportType transportType){
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.path("features").isEmpty()){
                logger.warn("Error parsing distance and duration from tour information from: " + from + " to: " + to);
                throw new IllegalArgumentException();
            }

            JsonNode summaryNode = rootNode.path("features").get(0).path("properties").path("summary");

            double distance = summaryNode.path("distance").asDouble();
            Long duration = summaryNode.path("duration").asLong();

            return new TourModel(
                    name.trim(),
                    description.trim(),
                    from.trim(),
                    to.trim(),
                    transportType.getApiParameter(),
                    distance,
                    duration,
                    jsonResponse
            );
        } catch (IOException e) {
            logger.warn("Error reading jsonResponse: " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public boolean searchTours(String query) {
        List<TourModel> searchResults = tourRepository.searchTours(query);

        if (searchResults.isEmpty()) {
            return false;
        } else {
            tours.setAll(searchResults);
        }
        return true;
    }

}