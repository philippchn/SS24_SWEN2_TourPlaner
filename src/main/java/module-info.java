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
    requires org.apache.logging.log4j;
    requires kernel;
    requires layout;

    opens org.technikum.tourplaner to javafx.fxml;
    opens org.technikum.tourplaner.BL.controller to javafx.fxml;
    opens org.technikum.tourplaner.BL.models to org.hibernate.orm.core, com.fasterxml.jackson.databind;
    exports org.technikum.tourplaner;
    exports org.technikum.tourplaner.BL.controller;
    exports org.technikum.tourplaner.BL.models;
}