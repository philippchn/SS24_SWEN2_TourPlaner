package org.technikum.tourplaner.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TourLogModel {
    @Setter
    private StringProperty date;
    @Setter
    private StringProperty comment;
    @Setter
    private StringProperty difficulty;
    @Setter
    private StringProperty totalDistance;;
    @Setter
    private StringProperty totalTime;
    @Setter
    private StringProperty rating;

    public TourLogModel(String date, String comment, String difficulty, String totalDistance, String totalTime, String rating) {
        this.date = new SimpleStringProperty(date);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.totalDistance = new SimpleStringProperty(totalDistance);
        this.totalTime = new SimpleStringProperty(totalTime);
        this.rating = new SimpleStringProperty(rating);
    }
}
