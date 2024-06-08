module org.technikum.tourplaner {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens org.technikum.tourplaner to javafx.fxml;
    exports org.technikum.tourplaner;
    exports org.technikum.tourplaner.controller;
    opens org.technikum.tourplaner.controller to javafx.fxml;
    opens org.technikum.tourplaner.models to org.hibernate.orm.core;
}