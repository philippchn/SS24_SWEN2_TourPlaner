package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.technikum.tourplaner.EViews;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ImageView mapImageView;

    private final SimpleStringProperty nameProperty = new SimpleStringProperty();
    private final SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private final SimpleStringProperty fromProperty = new SimpleStringProperty();
    private final SimpleStringProperty toProperty = new SimpleStringProperty();
    private final SimpleStringProperty transportTypeProperty = new SimpleStringProperty();

    private final TourViewModel tourViewModel;

    public TourListController(TourViewModel tourViewModel) {
        this.tourViewModel = tourViewModel;
    }

    @FXML
    private void initialize() {
        bindProperties();

        saveButton.setOnAction(event -> saveTour());
        tourListView.setOnMouseClicked(event -> clickElement());
        deleteButton.setOnAction(event -> deleteTour());
        tourListView.setItems(tourViewModel.getTours());
        modifyButton.setOnAction(event -> openModifyTourPopup()); // Add action handler for modify button

    }

    private void bindProperties() {
        nameTextField.textProperty().bindBidirectional(nameProperty);
        descriptionTextField.textProperty().bindBidirectional(descriptionProperty);
        fromTextField.textProperty().bindBidirectional(fromProperty);
        toTextField.textProperty().bindBidirectional(toProperty);
        transportTypeTextField.textProperty().bindBidirectional(transportTypeProperty);
    }

    private void saveTour() {
        if (isValidInput()) {
            TourModel newTour = new TourModel(
                    nameTextField.getText().trim(),
                    descriptionTextField.getText().trim(),
                    fromTextField.getText().trim(),
                    toTextField.getText().trim(),
                    transportTypeTextField.getText().trim()
            );

            tourViewModel.addTour(newTour);

            clearInputFields();
        }
    }

    private boolean isValidInput() {
        if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
            setErrorMessage(nameTextField);
            return false;
        } else if (fromTextField.getText() == null || fromTextField.getText().isBlank()) {
            setErrorMessage(fromTextField);
            return false;
        } else if (toTextField.getText() == null || toTextField.getText().isBlank()) {
            setErrorMessage(toTextField);
            return false;
        } else if (transportTypeTextField.getText() == null || transportTypeTextField.getText().isBlank()) {
            setErrorMessage(transportTypeTextField);
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-ZäÄöÖüÜ]+$");
        Matcher validateFrom = pattern.matcher(fromTextField.getText().trim());
        Matcher validateTo = pattern.matcher(toTextField.getText().trim());

        if (!(validateFrom.matches() && validateTo.matches())) {
            statusMessageLabel.setTextFill(Color.RED);
            statusMessageLabel.setText("From and To must not contain numbers or special characters");
            return false;
        }

        statusMessageLabel.setTextFill(Color.BLACK);
        return true;
    }

    private void setErrorMessage(TextField missingTextField) {
        statusMessageLabel.setTextFill(Color.RED);
        statusMessageLabel.setText(missingTextField.getPromptText() + " must not be empty");
    }

    private void clearInputFields() {
        nameTextField.clear();
        descriptionTextField.clear();
        fromTextField.clear();
        toTextField.clear();
        transportTypeTextField.clear();
        statusMessageLabel.setText("Create new tour:");
    }

    private void clickElement() {
        if (tourListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        tourViewModel.setSelectedTourModel(tourListView.getSelectionModel().getSelectedItem());
        setDetailView(tourListView.getSelectionModel().getSelectedItem());
    }

    private void setDetailView(TourModel selectedItem)
    {
        detailViewName.setText("Name: " + selectedItem.getName().get());
        detailViewDescription.setText("Description: " + selectedItem.getTourDescription().get());
        detailViewFrom.setText("From: " + selectedItem.getFrom().get());
        detailViewTo.setText("To: " + selectedItem.getTo().get());
        detailViewTransportType.setText("Transport type: " + selectedItem.getTransportType().get());
//        detailViewDistance.setText("Distance: " + selectedItem.getDistance().get()); // TODO NOT YET IMPLEMENTED
//        detailViewEstimatedTime.setText("Estimated time: " + selectedItem.getEstimatedTime().get());
//        detailViewRouteInformation.setText("Route information: " + selectedItem.getRouteInformation().get());
        mapImageView.setImage(new Image(getClass().getResource("/org/technikum/tourplaner/img/mapPlaceholder.jpg").toExternalForm()));
    }

    private void deleteTour() {
        TourModel selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            tourViewModel.deleteTour(selectedTour);
        }
    }

    private void openModifyTourPopup() {
        TourModel selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            try {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(EViews.modifyTourPopup.getFilePath()));
                Parent root = loader.load();

                ModifyTourPopupController controller = loader.getController();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                controller.initData(selectedTour, stage, tourViewModel);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
