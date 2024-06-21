package org.technikum.tourplaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.technikum.tourplaner.controller.TourListController;
import org.technikum.tourplaner.controller.TourLogsController;
import org.technikum.tourplaner.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.repositories.TourLogRepository;
import org.technikum.tourplaner.repositories.TourRepository;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    @Getter
    private static Stage stg;

    @Override
    public void start(Stage stage) {
        logger.info("Application started");
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
            logger.fatal("Error while starting application: " + e.getMessage());
        } catch (JDBCConnectionException e) {
            logger.fatal("Database connection failed: " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void addTourSubView(Parent mainView) throws IOException
    {
        TourRepository tourRepository = new TourRepository();
        OpenRouteServiceClient openRouteServiceClient = new OpenRouteServiceClient();
        TourViewModel tourViewModel = new TourViewModel(tourRepository, openRouteServiceClient);

        FXMLLoader tourListLoader = new FXMLLoader(getClass().getResource(EViews.tourList.getFilePath()));
        TourListController tourListController = new TourListController(tourViewModel);
        tourListLoader.setController(tourListController);
        Parent tourListView = tourListLoader.load();

        FXMLLoader tourLogsLoader = new FXMLLoader(getClass().getResource(EViews.tourLogs.getFilePath()));
        TourLogRepository tourLogRepository = new TourLogRepository();
        TourLogsController tourLogsController = new TourLogsController(tourViewModel, tourLogRepository);
        tourLogsLoader.setController(tourLogsController);
        Parent tourLogsView = tourLogsLoader.load();

        HBox subView = (HBox) mainView.lookup("#subView");
        subView.getChildren().addAll(tourListView, tourLogsView);
    }

    public static void main(String[] args) {
        launch();
    }
}
