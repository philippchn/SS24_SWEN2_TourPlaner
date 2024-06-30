package org.technikum.tourplaner.BL.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "t_tourLogs")
public class TourLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private Integer difficulty;

    @Column(name = "total_distance")
    private Double totalDistance;

    @Column(name = "total_time")
    private Long totalTime;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "fk_tour")
    private Long tourId;

    public TourLogModel() {

    }

    public TourLogModel(LocalDate date, String comment, Integer difficulty, Double totalDistance, Long totalTime, Integer rating, Long tourId) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.rating = rating;
        this.tourId = tourId;
    }

    @JsonIgnore
    public String getObjectStringView () {
        return "TourLogModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", comment='" + comment + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", totalDistance='" + totalDistance + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", rating='" + rating + '\'' +
                ", tourId=" + tourId +
                '}';
    }
}
