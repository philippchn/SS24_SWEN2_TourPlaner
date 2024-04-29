package org.technikum.tourplaner.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.technikum.tourplaner.MainApplication;

import java.io.IOException;

@Deprecated
public class AfterLoginController {

    MainApplication m = new MainApplication();

    @FXML
    private Button logOutButton;

    @FXML
    private Button closeButton;

    @FXML
    void logOut(ActionEvent event) throws IOException {
        m.changeScene("mainWindow.fxml");
    }

    @FXML
    void closeApp(ActionEvent event) throws IOException {
        MainApplication.getStg().close();
    }

}