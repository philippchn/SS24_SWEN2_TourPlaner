package org.technikum.tourplaner.BL.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TourModelTest {

    private TourModel tour;

    @BeforeEach
    void setUp() {
        tour = new TourModel();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(tour);
    }

    @Test
    void testAllArgsConstructor() {
        //Given/When
        tour = new TourModel("Tour Name", "A description", "Start", "End", "Car", 100.0, 120L, "Route info");

        //Then
        assertEquals("Tour Name", tour.getName());
        assertEquals("A description", tour.getTourDescription());
        assertEquals("Start", tour.getFrom());
        assertEquals("End", tour.getTo());
        assertEquals("Car", tour.getTransportType());
        assertEquals(100.0, tour.getDistance());
        assertEquals(120L, tour.getEstimatedTime());
        assertEquals("Route info", tour.getRouteInformation());
    }

    @Test
    void testGettersAndSetters() {
        //Given/When
        tour.setName("Tour Name");
        tour.setTourDescription("A description");
        tour.setFrom("Start");
        tour.setTo("End");
        tour.setTransportType("Car");
        tour.setDistance(100.0);
        tour.setEstimatedTime(120L);
        tour.setRouteInformation("Route info");

        //Then
        assertEquals("Tour Name", tour.getName());
        assertEquals("A description", tour.getTourDescription());
        assertEquals("Start", tour.getFrom());
        assertEquals("End", tour.getTo());
        assertEquals("Car", tour.getTransportType());
        assertEquals(100.0, tour.getDistance());
        assertEquals(120L, tour.getEstimatedTime());
        assertEquals("Route info", tour.getRouteInformation());
    }

    @Test
    void testGetObjectStringView() {
        //Given
        tour = new TourModel("Tour Name", "A description", "Start", "End", "Car", 100.0, 120L, "Route info");
        String expected = "TourModel{" +
                "id=1" +
                ", name='Tour Name'" +
                ", tourDescription='A description'" +
                ", from='Start'" +
                ", to='End'" +
                ", transportType='Car'" +
                ", distance='100.0'" +
                ", estimatedTime='120'" +
                ", routeInformation='Route info'" +
                ", tourLogsMap={}" +
                '}';

        //When
        tour.setId(1L);

        //Then
        assertEquals(expected, tour.getObjectStringView());
    }

    @Test
    void testToString() {
        //Given/When
        tour.setName("Tour Name");

        //Then
        assertEquals("Tour Name", tour.toString());
    }

    @Test
    void testAddTourLog() {
        //Given
        TourLogModel tourLog = new TourLogModel();

        //When
        tour.addTourLog("Tour Name", tourLog);
        Map<String, List<TourLogModel>> tourLogsMap = tour.getTourLogsMap();

        //Then
        assertTrue(tourLogsMap.containsKey("Tour Name"));
        assertEquals(1, tourLogsMap.get("Tour Name").size());
        assertEquals(tourLog, tourLogsMap.get("Tour Name").get(0));
    }

    @Test
    void testSetAndGetId() {
        //Given/When
        tour.setId(2L);

        //Then
        assertEquals(2L, tour.getId());
    }
}