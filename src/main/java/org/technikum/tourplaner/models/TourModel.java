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
    private StringProperty routeInformation;
    @Getter
    private Map<String, List<TourLogModel>> tourLogsMap = new HashMap<>();

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

    public void addTourLog(String tourName, TourLogModel tourLog) {
        List<TourLogModel> logs = tourLogsMap.computeIfAbsent(tourName, k -> new ArrayList<>());
        logs.add(tourLog);
    }
}
