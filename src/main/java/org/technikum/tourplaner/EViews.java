package org.technikum.tourplaner;

public enum EViews {
    mainView("mainView.fxml"),
    searchBar("searchBar.fxml"),
    menuBar("menuBar.fxml"),
    tourSubView("tourSubView.fxml"),
    tourList("tourList.fxml"),
    tourLogs("tourLogs.fxml"),
    modifyTourPopup("modifyTourPopup.fxml"),
    modifyTourLogPopup("modifyTourLogsPopup.fxml");

    private final String fileName;

    EViews(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFilePath()
    {
        return "View/" + fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}
