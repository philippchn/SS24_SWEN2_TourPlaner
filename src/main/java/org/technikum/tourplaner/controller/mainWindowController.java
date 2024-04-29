package org.technikum.tourplaner.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.technikum.tourplaner.MainApplication;
import org.technikum.tourplaner.viewmodels.LoginViewModel;

import java.io.IOException;

@Deprecated
public class mainWindowController {
    MainApplication mainApp = new MainApplication();
    private final LoginViewModel loginViewModel = new LoginViewModel();
    public mainWindowController() {

    }

    @FXML
    private Button logInButton;

    @FXML
    private PasswordField pwd;

    @FXML
    private TextField username;

    @FXML
    private Label warningLabel;

    @FXML
    void userLogIn(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        if(loginViewModel.checkLogin(username.getText(), pwd.getText())){
            warningLabel.setText("Success!");
            mainApp.changeScene("afterLogin.fxml");
        } else if (username.getText().isEmpty() || pwd.getText().isEmpty()) {
            warningLabel.setText("Please enter your data!");
        }
        else{
            warningLabel.setText("Wrong username or password");
        }
    }
}