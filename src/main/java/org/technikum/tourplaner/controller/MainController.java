package org.technikum.tourplaner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.technikum.tourplaner.viewmodels.MainViewModel;

public class MainController
{
    private final MainViewModel mainViewModel = new MainViewModel();

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        if (mainViewModel.changeText()) {
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }
}