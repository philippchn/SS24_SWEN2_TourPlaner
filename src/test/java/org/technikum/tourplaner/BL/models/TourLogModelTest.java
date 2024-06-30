package org.technikum.tourplaner.BL.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TourLogModelTest {

    private TourLogModel tourLog;

    @BeforeEach
    void setUp() {
        tourLog = new TourLogModel();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(tourLog);
    }

    @Test
    void testAllArgsConstructor() {
        //Given//When
        LocalDate date = LocalDate.now();
        tourLog = new TourLogModel(date, "Great tour", 3, 12.5, 3600L, 5, 1L);

        //Then
        assertEquals(date, tourLog.getDate());
        assertEquals("Great tour", tourLog.getComment());
        assertEquals(3, tourLog.getDifficulty());
        assertEquals(12.5, tourLog.getTotalDistance());
        assertEquals(3600L, tourLog.getTotalTime());
        assertEquals(5, tourLog.getRating());
        assertEquals(1L, tourLog.getTourId());
    }

    @Test
    void testGettersAndSetters() {
        //Given/When
        LocalDate date = LocalDate.now();
        tourLog.setDate(date);
        tourLog.setComment("Great tour");
        tourLog.setDifficulty(3);
        tourLog.setTotalDistance(12.5);
        tourLog.setTotalTime(3600L);
        tourLog.setRating(5);
        tourLog.setTourId(1L);

        //Then
        assertEquals(date, tourLog.getDate());
        assertEquals("Great tour", tourLog.getComment());
        assertEquals(3, tourLog.getDifficulty());
        assertEquals(12.5, tourLog.getTotalDistance());
        assertEquals(3600L, tourLog.getTotalTime());
        assertEquals(5, tourLog.getRating());
        assertEquals(1L, tourLog.getTourId());
    }

    @Test
    void testGetObjectStringView() {
        //Given
        LocalDate date = LocalDate.now();
        tourLog = new TourLogModel(date, "Great tour", 3, 12.5, 3600L, 5, 1L);
        String expected = "TourLogModel{" +
                "id=1" +
                ", date='" + date + '\'' +
                ", comment='Great tour'" +
                ", difficulty='3'" +
                ", totalDistance='12.5'" +
                ", totalTime='3600'" +
                ", rating='5'" +
                ", tourId=1" +
                '}';

        //When
        tourLog.setId(1L);

        //Then
        assertEquals(expected, tourLog.getObjectStringView());
    }

    @Test
    void testSetAndGetId() {
        //Given/When
        tourLog.setId(2L);

        //Then
        assertEquals(2L, tourLog.getId());
    }
}