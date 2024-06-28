package org.technikum.tourplaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.technikum.tourplaner.BL.controller.SearchBarController;
import org.technikum.tourplaner.BL.controller.TourListController;
import org.technikum.tourplaner.BL.controller.TourLogsController;
import org.technikum.tourplaner.BL.viewmodels.SearchViewModel;
import org.technikum.tourplaner.BL.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.BL.viewmodels.TourViewModel;
import org.technikum.tourplaner.DAL.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.DAL.repositories.EntityManagerFactoryProvider;
import org.technikum.tourplaner.DAL.repositories.RepositoryFactory;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    @Getter
    private static Stage stg;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        if (!propertiesFileIsValid()){
            logger.info("Application start aborted");
            return;
        }

        logger.info("Application started");

        try {
            ResourceBundle resourceBundleProperties = ResourceBundle.getBundle("cfg");
            String language = resourceBundleProperties.getString("language");

            stg = stage;
            ResourceBundle resourceBundleLanguage = ResourceBundle.getBundle("org.technikum.tourplaner.View." + "gui_strings", createLocale(language));
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource(EViews.mainView.getFilePath()),
                    resourceBundleLanguage,
                    new JavaFXBuilderFactory()
            );
            Parent mainView = fxmlLoader.load();

            addTourSubView(mainView, resourceBundleLanguage);
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

    @Override
    public void stop() {
        EntityManagerFactoryProvider.closeEntityManagerFactories();
    }

    private Locale createLocale(String language) {
        if (language.equalsIgnoreCase("ENGLISH")) {
            return Locale.ENGLISH;
        } else if (language.equalsIgnoreCase("GERMAN")) {
            return Locale.GERMAN;
        }
        return Locale.GERMAN;
    }

    private boolean propertiesFileIsValid() {
        try{
            ResourceBundle resourceBundle = ResourceBundle.getBundle("cfg");
            if (resourceBundle.getString("openRouteServiceAPIKey").isEmpty()){
                logger.fatal("cfg.properties not properly set up: Please define an API Key");
                return false;
            }
            if (resourceBundle.getString("dbUser").isEmpty()){
                logger.fatal("cfg.properties not properly set up: Please define a dbUser");
                return false;
            }
            if (resourceBundle.getString("dbPassword").isEmpty()){
                logger.fatal("cfg.properties not properly set up: Please define a dbPassword");
                return false;
            }
            if (resourceBundle.getString("language").isEmpty()){
                logger.fatal("cfg.properties not properly set up: Please define a language");
                return false;
            }
            if (!resourceBundle.getString("language").equalsIgnoreCase("ENGLISH") && !resourceBundle.getString("language").equalsIgnoreCase("GERMAN")){
                logger.fatal("cfg.properties not properly set up: Please define a valid language: ENGLISH/GERMAN");
                return false;
            }
        }catch (MissingResourceException e){
            logger.fatal("cfg.properties not properly set up: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void addTourSubView(Parent mainView, ResourceBundle resourceBundle) throws IOException {
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        TourRepository tourRepository = repositoryFactory.createTourRepository();
        TourLogRepository tourLogRepository = repositoryFactory.createTourLogRepository();
        OpenRouteServiceClient openRouteServiceClient = new OpenRouteServiceClient();
        TourViewModel tourViewModel = new TourViewModel(tourRepository, tourLogRepository, openRouteServiceClient);

        FXMLLoader tourListLoader = new FXMLLoader(
                getClass().getResource(EViews.tourList.getFilePath()),
                resourceBundle,
                new JavaFXBuilderFactory()
        );
        TourListController tourListController = new TourListController(tourViewModel);
        tourListLoader.setController(tourListController);
        Parent tourListView = tourListLoader.load();

        FXMLLoader tourLogsLoader = new FXMLLoader(
                getClass().getResource(EViews.tourLogs.getFilePath()),
                resourceBundle,
                new JavaFXBuilderFactory()
        );
        TourLogsController tourLogsController = new TourLogsController(tourRepository, tourViewModel, tourLogRepository);
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository,tourLogRepository,tourViewModel);
        tourLogsLoader.setController(tourLogsController);
        Parent tourLogsView = tourLogsLoader.load();

        HBox subView = (HBox) mainView.lookup("#subView");
        subView.getChildren().addAll(tourListView, tourLogsView);

        FXMLLoader searchBarLoader = new FXMLLoader(
                getClass().getResource(EViews.searchBar.getFilePath()),
                resourceBundle,
                new JavaFXBuilderFactory()
        );
        SearchBarController searchBarController = new SearchBarController(tourViewModel);
        searchBarLoader.setController(searchBarController);
        Parent searchBar = searchBarLoader.load();
        searchBarController.setSearchViewModel(new SearchViewModel(tourViewModel, tourLogViewModel));

        VBox root = (VBox) mainView;
        root.getChildren().add(1, searchBar);
    }
}
