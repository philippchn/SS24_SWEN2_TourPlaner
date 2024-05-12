package org.technikum.tourplaner.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class TourModel {
    @Getter @Setter
    private StringProperty name = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty tourDescription = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty from = new SimpleStringProperty("");;
    @Getter @Setter
    private StringProperty to = new SimpleStringProperty("");;
    @Getter @Setter
    private StringProperty transportType = new SimpleStringProperty("");
    @Getter @Setter
    private StringProperty distance;
    @Getter @Setter
    private StringProperty restimatedTime;
    @Getter @Setter
    private StringProperty routeInformation; // an image with the tour map

    public TourModel(String name, String tourDescription, String from, String to, String transportType)
    {
        this.name.set(name);
        this.tourDescription.set(tourDescription);
        this.from.set(from);
        this.to.set(to);
        this.transportType.set(transportType);
    }

    @Override
    public String toString()
    {
        return this.name.getValue();
    }
}
