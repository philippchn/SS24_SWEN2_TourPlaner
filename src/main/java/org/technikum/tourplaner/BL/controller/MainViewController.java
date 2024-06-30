package org.technikum.tourplaner.BL.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.technikum.tourplaner.MainApplication;

public class MainViewController {
    @FXML
    private Button toggleNightModeButton;

    private boolean nightMode = false;

    @FXML
    private void initialize() {
        updateButtonText();
    }

    @FXML
    private void toggleNightMode() {
        nightMode = !nightMode;
        MainApplication.switchToNightMode(nightMode);
        updateButtonText();
    }

    private void updateButtonText() {
        toggleNightModeButton.setText(nightMode ? "Switch to Light Mode" : "Switch to Night Mode");
    }
}
