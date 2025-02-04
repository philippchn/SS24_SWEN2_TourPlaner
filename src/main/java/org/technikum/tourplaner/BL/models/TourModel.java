package org.technikum.tourplaner.BL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Double distance;

    @Column(name = "estimated_time")
    private Long estimatedTime;

    @Column(name = "route_information", columnDefinition = "TEXT")
    private String routeInformation;

    @Transient
    private Map<String, List<TourLogModel>> tourLogsMap = new HashMap<>();

    public TourModel(){

    }

    public TourModel(String name, String tourDescription, String from, String to, String transportType, Double distance, Long estimatedTime, String routeInformation) {
        this.name = name;
        this.tourDescription = tourDescription;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
    }

    @JsonIgnore
    public String getObjectStringView() {
        return "TourModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tourDescription='" + tourDescription + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", transportType='" + transportType + '\'' +
                ", distance='" + distance + '\'' +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", routeInformation='" + routeInformation + '\'' +
                ", tourLogsMap=" + tourLogsMap +
                '}';
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
