package org.technikum.tourplaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Getter
    private static Stage stg;

    @Override
    public void start(Stage stage) {
        try {
            stg = stage;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("LogInDemo!");

            // Guard against potential NullPointerException
            Image favicon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/favicon.png")));
            stage.getIcons().add(favicon);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as necessary
        }
    }

    public void changeScene(String fxml) {
        try {
            stg.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml))));
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as necessary
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
