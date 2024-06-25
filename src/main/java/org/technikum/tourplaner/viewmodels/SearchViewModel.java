package org.technikum.tourplaner.viewmodels;

public class SearchViewModel {
    private final TourViewModel tourViewModel;
    private final TourLogViewModel tourLogViewModel;

    public SearchViewModel(TourViewModel tourViewModel, TourLogViewModel tourLogViewModel) {
        this.tourViewModel = tourViewModel;
        this.tourLogViewModel = tourLogViewModel;
    }

    public void performSearch(String query) {
        tourViewModel.searchTours(query);
        tourLogViewModel.searchTourLogs(query);
    }
}
