package org.technikum.tourplaner;

public enum EViews {
    mainWindow("mainUi.fxml");

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
