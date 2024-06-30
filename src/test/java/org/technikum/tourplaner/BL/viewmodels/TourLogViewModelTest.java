package org.technikum.tourplaner.BL.viewmodels;

import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.models.TourModel;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class TourLogViewModelTest {
    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourLogRepository tourLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadTourLogs_shouldLoadLogsWhenTourIsSelected() {
        //Given
        TourModel selectedTour = new TourModel();
        selectedTour.setId(1L);

        TourLogModel tourLog = new TourLogModel(LocalDate.now(), "comment", 5, 10.0, 60L, 4, 1L);
        when(tourLogRepository.getTourLogsByTourId("1")).thenReturn(Collections.singletonList(tourLog));

        TourViewModel tourViewModelMock = mock(TourViewModel.class);
        when((tourViewModelMock.selectedTourModelProperty())).thenReturn(new SimpleObjectProperty<>());
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModelMock);

        //When
        tourLogViewModel.loadTourLogs(selectedTour);

        //Then
        assertFalse(tourLogViewModel.getTourLogs().isEmpty());
        assertEquals(1, tourLogViewModel.getTourLogs().size());
    }

    @Test
    void addTourLog_shouldAddTourLogWhenInputIsValid() {
        //Given
        TourModel selectedTour = new TourModel();
        selectedTour.setId(1L);

        TourViewModel tourViewModelMock = mock(TourViewModel.class);
        when(tourViewModelMock.getSelectedTourModel()).thenReturn(selectedTour);
        when((tourViewModelMock.selectedTourModelProperty())).thenReturn(new SimpleObjectProperty<>());

        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModelMock);
        tourLogViewModel.getDateProperty().set(LocalDate.now());
        tourLogViewModel.getCommentProperty().set("A good comment");
        tourLogViewModel.getDifficultyProperty().set("5");
        tourLogViewModel.getTotalDistanceProperty().set("10.0");
        tourLogViewModel.getTotalTimeProperty().set("60");
        tourLogViewModel.getRatingProperty().set("4");

        //When
        tourLogViewModel.addTourLog();

        //Then
        verify(tourLogRepository, times(1)).save(any(TourLogModel.class));
        assertFalse(tourLogViewModel.getTourLogs().isEmpty());
    }

    @Test
    void deleteTourLog_shouldDeleteSelectedTourLog() {
        //Given
        TourLogModel tourLogModel = new TourLogModel();
        tourLogModel.setId(1L);

        TourViewModel tourViewModelMock = mock(TourViewModel.class);
        when((tourViewModelMock.selectedTourModelProperty())).thenReturn(new SimpleObjectProperty<>());

        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModelMock);
        tourLogViewModel.setSelectedTourLogModel(tourLogModel);

        //When
        tourLogViewModel.deleteTourLog();

        //Then
        verify(tourLogRepository, times(1)).deleteById(1L);
        assertTrue(tourLogViewModel.getTourLogs().isEmpty());
    }

    @Test
    void selectTourLog_shouldUpdateDetailView() {
        //Given
        TourLogModel tourLogModel = new TourLogModel(LocalDate.now(), "comment", 5, 10.0, 60L, 4, 1L);

        TourViewModel tourViewModelMock = mock(TourViewModel.class);
        when((tourViewModelMock.selectedTourModelProperty())).thenReturn(new SimpleObjectProperty<>());

        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModelMock);
        tourLogViewModel.setSelectedTourLogModel(tourLogModel);

        //When
        tourLogViewModel.selectTourLog();

        //Then
        assertEquals("Date: " + tourLogModel.getDate(), tourLogViewModel.getDetailViewDateProperty().get());
        assertEquals("Comment: " + tourLogModel.getComment(), tourLogViewModel.getDetailViewCommentProperty().get());
    }

    @Test
    void updateTourLog_shouldUpdateTourLog() {
        //Given
        TourLogModel tourLogModel = new TourLogModel();
        tourLogModel.setId(1L);

        TourViewModel tourViewModelMock = mock(TourViewModel.class);
        when((tourViewModelMock.selectedTourModelProperty())).thenReturn(new SimpleObjectProperty<>());

        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourRepository, tourLogRepository, tourViewModelMock);
        tourLogViewModel.setSelectedTourLogModel(tourLogModel);

        //When
        tourLogViewModel.updateTourLog(tourLogModel);

        //Then
        verify(tourLogRepository, times(1)).updateById(1L, tourLogModel);
    }
}