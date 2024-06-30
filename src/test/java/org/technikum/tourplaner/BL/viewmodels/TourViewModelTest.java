package org.technikum.tourplaner.BL.viewmodels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.technikum.tourplaner.BL.models.TourModel;
import org.technikum.tourplaner.DAL.openrouteservice.ETransportType;
import org.technikum.tourplaner.DAL.openrouteservice.OpenRouteServiceClient;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourViewModelTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourLogRepository tourLogRepository;

    @Mock
    private OpenRouteServiceClient openRouteServiceClient;

    @InjectMocks
    private TourViewModel tourViewModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourViewModel = new TourViewModel(tourRepository, tourLogRepository, openRouteServiceClient);
    }

    @Test
    void loadToursFromDatabase_shouldLoadAllTours() {
        // Given
        TourModel tour = new TourModel();
        when(tourRepository.getAllTours()).thenReturn(Collections.singletonList(tour));

        // When
        tourViewModel.loadToursFromDatabase();

        // Then
        assertFalse(tourViewModel.getTours().isEmpty());
        assertEquals(1, tourViewModel.getTours().size());
    }

    @Test
    void deleteTour_shouldDeleteSelectedTour() {
        // Given
        TourModel tour = new TourModel();
        tour.setId(1L);
        tourViewModel.selectedTourModelProperty().set(tour);

        // When
        tourViewModel.deleteTour();

        // Then
        verify(tourRepository, times(1)).deleteById(1L);
        verify(tourLogRepository, times(1)).deleteByFK(1L);
        assertTrue(tourViewModel.getTours().isEmpty());
    }

    @Test
    void searchTours_shouldReturnSearchResults() {
        // Given
        TourModel tour = new TourModel();
        when(tourRepository.searchTours(anyString())).thenReturn(Collections.singletonList(tour));

        // When
        boolean result = tourViewModel.searchTours("query");

        // Then
        assertTrue(result);
        assertFalse(tourViewModel.getTours().isEmpty());
    }

    @Test
    void searchTours_shouldReturnEmptyWhenNoResultsFound() {
        // Given
        when(tourRepository.searchTours(anyString())).thenReturn(Collections.emptyList());

        // When
        boolean result = tourViewModel.searchTours("query");

        // Then
        assertFalse(result);
        assertTrue(tourViewModel.getTours().isEmpty());
    }

    @Test
    void parseRouteResponse_shouldParseJsonResponseCorrectly() throws Exception {
        // Given
        String jsonResponse = "{ \"features\": [ { \"properties\": { \"summary\": { \"distance\": 100.0, \"duration\": 60 } } } ] }";
        String name = "Tour Name";
        String description = "Description";
        String from = "From Location";
        String to = "To Location";
        ETransportType transportType = ETransportType.Cycling;

        // When
        TourModel tourModel = tourViewModel.parseRouteResponse(jsonResponse, name, description, from, to, transportType);

        // Then
        assertEquals(name, tourModel.getName());
        assertEquals(description, tourModel.getTourDescription());
        assertEquals(from, tourModel.getFrom());
        assertEquals(to, tourModel.getTo());
        assertEquals(transportType.getApiParameter(), tourModel.getTransportType());
        assertEquals(100.0, tourModel.getDistance());
        assertEquals(60L, tourModel.getEstimatedTime());
    }

    @Test
    void parseRouteResponse_shouldThrowExceptionForInvalidJson() {
        // Given
        String invalidJsonResponse = "{ invalid json }";
        String name = "Tour Name";
        String description = "Description";
        String from = "From Location";
        String to = "To Location";
        ETransportType transportType = ETransportType.Cycling;

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> {
            tourViewModel.parseRouteResponse(invalidJsonResponse, name, description, from, to, transportType);
        });
    }
}
