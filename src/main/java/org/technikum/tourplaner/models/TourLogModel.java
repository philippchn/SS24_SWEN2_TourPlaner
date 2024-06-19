package org.technikum.tourplaner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_tourLogs")
public class TourLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "total_distance")
    private String totalDistance;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "rating")
    private String rating;

    @Column(name = "fk_tour")
    private Long tourId;

    TourLogModel() {

    }

    public TourLogModel(String date, String comment, String difficulty, String totalDistance, String totalTime, String rating, Long tourId) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.rating = rating;
        this.tourId = tourId;
    }

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
