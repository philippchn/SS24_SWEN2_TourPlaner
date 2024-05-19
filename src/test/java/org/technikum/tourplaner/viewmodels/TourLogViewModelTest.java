package org.technikum.tourplaner.viewmodels;
import javafx.beans.property.ObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.technikum.tourplaner.models.TourLogModel;
import org.technikum.tourplaner.models.TourModel;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.viewmodels.TourViewModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TourLogViewModelTest {

    @Mock
    private TourViewModel tourViewModel;

    @Mock
    private ObjectProperty<TourModel> selectedTourModelPropertyMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(tourViewModel.selectedTourModelProperty()).thenReturn(selectedTourModelPropertyMock);
    }

    @Test
    public void testInitialization() {
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourViewModel);
        assertEquals(tourViewModel, tourLogViewModel.getTourViewModel());
        assertEquals(0, tourLogViewModel.getTourLogs().size());
    }


    @Test
    public void testSelectTourLog() {
        TourViewModel tourViewModel = new TourViewModel(/* pass necessary parameters */);
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourViewModel);

        TourLogModel tourLogModel = new TourLogModel("testdate", "testcomment", "testdifficulty", "testtotalDistance", "testtotaltTotaltime", "testRating");
        tourLogViewModel.getTourLogs().add(tourLogModel);

        tourLogViewModel.setSelectedTourLogModel(tourLogModel);
        tourLogViewModel.selectTourLog();

        assertEquals("Date: testdate", tourLogViewModel.getDetailViewDateProperty().get());
        assertEquals("Comment: testcomment", tourLogViewModel.getDetailViewCommentProperty().get());
        assertEquals("Difficulty: testdifficulty", tourLogViewModel.getDetailViewDifficultyProperty().get());
        assertEquals("Total distance: testtotalDistance", tourLogViewModel.getDetailViewTotalDistanceProperty().get());
        assertEquals("Total time: testtotaltTotaltime", tourLogViewModel.getDetailViewTotalTimeProperty().get());
        assertEquals("Rating: testRating", tourLogViewModel.getDetailViewRatingProperty().get());
    }

    @Test
    public void testAddTourLog() {
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourViewModel);
        TourLogModel tourLogModel = new TourLogModel("2024-05-20", "Great tour!", "Medium", "10 km", "2 hours", "4");
        tourLogViewModel.getTourLogs().add(tourLogModel);

        // Assert that the size of the tour logs list is 1
        assertEquals(1, tourLogViewModel.getTourLogs().size());
    }

    @Test
    public void testDeleteTourLog() {
        TourLogViewModel tourLogViewModel = new TourLogViewModel(tourViewModel);

        TourLogModel tourLogModelToDelete = new TourLogModel("2024-05-20", "LogtoDelete", "Medium", "10 km", "2 hours", "4");
        TourLogModel anotherTourLogModel = new TourLogModel("2024-05-21", "LogtoKeep", "Easy", "5 km", "1 hour", "5");

        tourLogViewModel.getTourLogs().addAll(tourLogModelToDelete, anotherTourLogModel);

        tourLogViewModel.setSelectedTourLogModel(tourLogModelToDelete);
        tourLogViewModel.deleteTourLog();

        assertFalse(tourLogViewModel.getTourLogs().contains(tourLogModelToDelete));

        assertTrue(tourLogViewModel.getTourLogs().contains(anotherTourLogModel));
    }

}
