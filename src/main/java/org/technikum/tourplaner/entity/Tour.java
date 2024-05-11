package org.technikum.tourplaner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tour {

    @Getter
    private String name;
    @Getter
    private String tourDescription;
    @Getter
    private String from;
    @Getter
    private String to;
    @Getter
    private String transportType;
    @Getter
    private Double distance;
    @Getter
    private Long restimatedTime;
    @Getter
    private String routeInformation; // an image with the tour map

    public Tour(String name, String description, String from, String to, String transportType) {
        this.name = name;
        this.tourDescription = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
    }
}
