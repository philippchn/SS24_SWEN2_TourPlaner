package org.technikum.tourplaner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "t_tours")
public class TourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tour_description")
    private String tourDescription;

    @Column(name = "start_location")
    private String from;

    @Column(name = "end_location")
    private String to;

    @Column(name = "transport_type")
    private String transportType;

    @Column(name = "distance")
    private String distance;

    @Column(name = "estimated_time")
    private String estimatedTime;

    @Column(name = "route_information")
    private String routeInformation;

    @Transient
    private Map<String, List<TourLogModel>> tourLogsMap = new HashMap<>();

    TourModel(){

    }

    public TourModel(String name, String tourDescription, String from, String to, String transportType) {
        this.name = name;
        this.tourDescription = tourDescription;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public void addTourLog(String tourName, TourLogModel tourLog) {
        List<TourLogModel> logs = tourLogsMap.computeIfAbsent(tourName, k -> new ArrayList<>());
        logs.add(tourLog);
    }
}
