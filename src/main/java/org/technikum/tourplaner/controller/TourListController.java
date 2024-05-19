package org.technikum.tourplaner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

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
    private TextField transportTypeTextField;
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
    }

    @FXML
    private void initialize() {
        bindProperties();

        saveButton.setOnAction(event -> saveTour());
        tourListView.setOnMouseClicked(event -> tableRowClicked());
        deleteButton.setOnAction(event -> deleteTour());
        tourListView.setItems(tourViewModel.getTours());
        modifyButton.setOnAction(event -> openModifyTourPopup());
    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(tourViewModel.nameProperty());
        descriptionTextField.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
        fromTextField.textProperty().bindBidirectional(tourViewModel.fromProperty());
        toTextField.textProperty().bindBidirectional(tourViewModel.toProperty());
        transportTypeTextField.textProperty().bindBidirectional(tourViewModel.transportTypeProperty());
        statusMessageLabel.textProperty().bindBidirectional(tourViewModel.statusMessageProperty());
        detailViewName.textProperty().bindBidirectional(tourViewModel.detailViewNameProperty());
        detailViewDescription.textProperty().bindBidirectional(tourViewModel.detailViewDescriptionProperty());
        detailViewFrom.textProperty().bindBidirectional(tourViewModel.detailViewFromProperty());
        detailViewTo.textProperty().bindBidirectional(tourViewModel.detailViewToProperty());
        detailViewTransportType.textProperty().bindBidirectional(tourViewModel.detailViewTransportTypeProperty());
        detailViewImage.imageProperty().bindBidirectional(tourViewModel.detailViewMapImageProperty());

        tourViewModel.selectedTourModelProperty().bind(tourListView.getSelectionModel().selectedItemProperty());
    }

    private void saveTour() {
        tourViewModel.addTour();
    }

    private void tableRowClicked() {
        tourViewModel.setCurrentlyClickedTour();
    }

    private void deleteTour() {
        tourViewModel.deleteTour();
    }

    private void openModifyTourPopup() {
        tourViewModel.updateTour();
    }
}
