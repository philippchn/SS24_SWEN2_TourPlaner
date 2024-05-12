package org.technikum.tourplaner.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class TourLogModel {
    @Getter @Setter
    private StringProperty date = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty comment = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty difficulty = new SimpleStringProperty("");;
    @Getter @Setter
    private StringProperty totalDistance = new SimpleStringProperty("");;
    @Getter @Setter
    private StringProperty totalTime = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty rating = new SimpleStringProperty("");
}
