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
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.controller.ModifyTourPopupController;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.repositories.TourRepository;

import java.io.IOException;
import java.util.List;

public class TourViewModel {
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
    private final SimpleStringProperty transportTypeProperty = new SimpleStringProperty();
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
            String transportType = transportTypeProperty.get();

            String routeInformation = openRouteServiceClient.getTourInformation(from, to, "driving-car");
            TourModel newTour = parseRouteResponse(routeInformation, name, description, from, to, transportType);

            tours.add(newTour);
            tourRepository.save(newTour);
            clearInputFields();
        } catch (IllegalArgumentException | IOException e) {
            showErrorMessage("Failed to add tour: " + e.getMessage());
        }
    }

    private boolean allFieldsSet() {
        if (nameProperty.get() == null || nameProperty.get().isBlank()) {
            showErrorMessage("Name must not be empty");
            return false;
        } else if (descriptionProperty.get() == null || descriptionProperty.get().isBlank()) {
            showErrorMessage("Description must not be empty");
            return false;
        } else if (fromProperty.get() == null || fromProperty.get().isBlank()) {
            showErrorMessage("From must not be empty");
            return false;
        } else if (toProperty.get() == null || toProperty.get().isBlank()) {
            showErrorMessage("To must not be empty");
            return false;
        } else if (transportTypeProperty.get() == null || transportTypeProperty.get().isBlank()) {
            showErrorMessage("Transport type must not be empty");
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

                String updatedTransportType = transportTypeProperty.get();

                String routeInformation = openRouteServiceClient.getTourInformation(updatedFrom, updatedTo, "driving-car");
                TourModel updatedTour = parseRouteResponse(routeInformation, updatedName, updatedDescription, updatedFrom, updatedTo, updatedTransportType);

                tourRepository.updateById(selectedTour.getId(), updatedTour);

                updateDisplayedTourList(updatedTour);

                setDetailView(updatedTour);
            } catch (IOException e) {
                e.printStackTrace();
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

    public TourModel parseRouteResponse(String jsonResponse, String name, String description, String from, String to, String transportType) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.path("features").isEmpty()){
                throw new IllegalArgumentException("Error parsing distance and duration from tour information");
            }

            JsonNode summaryNode = rootNode.path("features").get(0).path("properties").path("summary");

            double distance = summaryNode.path("distance").asDouble();
            double duration = summaryNode.path("duration").asDouble();

            return new TourModel(
                    name.trim(),
                    description.trim(),
                    from.trim(),
                    to.trim(),
                    transportType.trim(),
                    String.valueOf(distance),
                    String.valueOf(duration),
                    jsonResponse
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error parsing route response: " + e.getMessage());
        }
    }
}