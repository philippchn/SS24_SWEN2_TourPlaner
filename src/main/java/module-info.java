module org.technikum.tourplaner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.technikum.tourplaner to javafx.fxml;
    exports org.technikum.tourplaner;
    exports org.technikum.tourplaner.controller;
    opens org.technikum.tourplaner.controller to javafx.fxml;
}