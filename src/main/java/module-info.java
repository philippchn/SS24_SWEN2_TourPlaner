module org.technikum.tourplaner {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;

    opens org.technikum.tourplaner to javafx.fxml;
    exports org.technikum.tourplaner;
    exports org.technikum.tourplaner.controller;
    opens org.technikum.tourplaner.controller to javafx.fxml;
}