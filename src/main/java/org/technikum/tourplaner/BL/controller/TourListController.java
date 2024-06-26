package org.technikum.tourplaner.BL.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.technikum.tourplaner.BL.models.TourModel;
import org.technikum.tourplaner.BL.viewmodels.TourViewModel;
import org.technikum.tourplaner.DAL.openrouteservice.ETransportType;

public class TourListController {
    @FXML
    private VBox rootVbox;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private ComboBox<ETransportType> transportTypeBox;
    @FXML
    private Button saveButton;
    @FXML
    private Label statusMessageLabel;
    @FXML
    private ListView<TourModel> tourListView;
    @FXML
    private Text detailViewName;
    @FXML
    private Text detailViewDescription;
    @FXML
    private Text detailViewFrom;
    @FXML
    private Text detailViewTo;
    @FXML
    private Text detailViewTransportType;
    @FXML
    private Text detailViewDistance;
    @FXML
    private Text detailViewEstimatedTime;
    @FXML
    private Text detailViewRouteInformation;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;
    @FXML
    private ImageView detailViewImage;

    private final TourViewModel tourViewModel;

    public TourListController(TourViewModel tourViewModel) {
        this.tourViewModel = tourViewModel;
        tourViewModel.loadToursFromDatabase();
    }

    @FXML
    private void initialize() {
        bindProperties();

        saveButton.setOnAction(event -> tourViewModel.addTour());
        tourListView.setOnMouseClicked(event -> tourViewModel.setCurrentlyClickedTour(tourListView.getSelectionModel().selectedItemProperty().get()));
        deleteButton.setOnAction(event ->tourViewModel.deleteTour());
        tourListView.setItems(tourViewModel.getTours());
        modifyButton.setOnAction(event -> {
            tourViewModel.updateTour();
            tourViewModel.loadToursFromDatabase();
        });
        transportTypeBox.getItems().addAll(ETransportType.values());
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(tourViewModel.nameProperty());
        descriptionTextField.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
        fromTextField.textProperty().bindBidirectional(tourViewModel.fromProperty());
        toTextField.textProperty().bindBidirectional(tourViewModel.toProperty());
        transportTypeBox.valueProperty().bindBidirectional(tourViewModel.transportTypeProperty());

        detailViewName.textProperty().bindBidirectional(tourViewModel.detailViewNameProperty());
        detailViewDescription.textProperty().bindBidirectional(tourViewModel.detailViewDescriptionProperty());
        detailViewFrom.textProperty().bindBidirectional(tourViewModel.detailViewFromProperty());
        detailViewTo.textProperty().bindBidirectional(tourViewModel.detailViewToProperty());
        detailViewTransportType.textProperty().bindBidirectional(tourViewModel.detailViewTransportTypeProperty());
        detailViewDistance.textProperty().bindBidirectional(tourViewModel.detailViewDistanceProperty());
        detailViewEstimatedTime.textProperty().bindBidirectional(tourViewModel.detailViewEstimatedTimeProperty());
        detailViewImage.imageProperty().bindBidirectional(tourViewModel.detailViewMapImageProperty());
    }
}
