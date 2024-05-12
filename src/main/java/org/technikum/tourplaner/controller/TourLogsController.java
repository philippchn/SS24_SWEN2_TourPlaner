package org.technikum.tourplaner.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

public class TourLogsController {
    @FXML
    private Label currentlySelectedTourLabel;
    @FXML
    private TableView logsTable;

    private final SimpleStringProperty selectedTourProperty = new SimpleStringProperty();

    private final TourViewModel tourViewModel;

    public TourLogsController(TourViewModel tourViewModel)
    {
        this.tourViewModel = tourViewModel;
    }

    @FXML
    private void initialize() {
        bindProperties();

        addListener();
    }

    private void bindProperties() {
        currentlySelectedTourLabel.textProperty().bindBidirectional(selectedTourProperty);
    }

    private void addListener() {
        tourViewModel.selectedTourModelProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends TourModel> observableValue, TourModel oldValue, TourModel newValue)
            {
                System.out.println("Changed");
                selectedTourProperty.set(newValue.toString());
            }
        });
    }
}
