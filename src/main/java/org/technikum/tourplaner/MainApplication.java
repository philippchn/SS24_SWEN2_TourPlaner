package org.technikum.tourplaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.technikum.tourplaner.controller.TourListController;
import org.technikum.tourplaner.controller.TourLogsController;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Getter
    private static Stage stg;

    @Override
    public void start(Stage stage) {
        try {
            stg = stage;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(EViews.mainView.getFilePath()));
            Parent mainView = fxmlLoader.load();

            addTourSubView(mainView);

            Scene scene = new Scene(mainView);
            stage.setTitle("TourPlanner");

            Image favicon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/favicon.png")));
            stage.getIcons().add(favicon);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Replace with log4j
        }
    }

    private void addTourSubView(Parent mainView) throws IOException
    {
        TourViewModel tourViewModel = new TourViewModel();

        FXMLLoader tourListLoader = new FXMLLoader(getClass().getResource(EViews.tourList.getFilePath()));
        TourListController tourListController = new TourListController(tourViewModel);
        tourListLoader.setController(tourListController);
        Parent tourListView = tourListLoader.load();

        FXMLLoader tourLogsLoader = new FXMLLoader(getClass().getResource(EViews.tourLogs.getFilePath()));
        TourLogsController tourLogsController = new TourLogsController(tourViewModel);
        tourLogsLoader.setController(tourLogsController);
        Parent tourLogsView = tourLogsLoader.load();

        HBox subView = (HBox) mainView.lookup("#subView");
        subView.getChildren().addAll(tourListView, tourLogsView);
    }

    public void changeScene(String fxml) {
        try {
            stg.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml))));
        } catch (IOException e) {
            e.printStackTrace(); // Replace with log4j
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
