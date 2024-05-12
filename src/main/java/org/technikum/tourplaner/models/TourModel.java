package org.technikum.tourplaner.models;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourModel {
    @Getter @Setter
    private StringProperty name;
    @Getter @Setter
    private StringProperty tourDescription;
    @Getter @Setter
    private StringProperty from;
    @Getter @Setter
    private StringProperty to;
    @Getter @Setter
    private StringProperty transportType;
    @Getter @Setter
    private StringProperty distance;
    @Getter @Setter
    private StringProperty estimatedTime;
    @Getter @Setter
    private StringProperty routeInformation;
    @Getter
    private Map<String, List<TourLogModel>> tourLogsMap = new HashMap<>();

    public TourModel(String name, String tourDescription, String from, String to, String transportType)
    {
        this.name = new SimpleStringProperty(name);
        this.tourDescription = new SimpleStringProperty(tourDescription);
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.transportType = new SimpleStringProperty(transportType);
    }

    @Override
    public String toString()
    {
        return this.name.getValue();
    }

    public void addTourLog(String tourName, TourLogModel tourLog) {
        List<TourLogModel> logs = tourLogsMap.computeIfAbsent(tourName, k -> new ArrayList<>());
        logs.add(tourLog);
    }
}
