package org.technikum.tourplaner;

public enum EViews {
    mainView("mainView.fxml"),
    searchBar("searchBar.fxml"),
    menuBar("menuBar.fxml"),
    tourList("tourList.fxml"),
    tourLogs("tourLogs.fxml");

    private final String fileName;

    EViews(String fileName)
    {
        this.fileName = fileName;
    }

    String getFileName()
    {
        return "View/" + fileName;
    }
}
