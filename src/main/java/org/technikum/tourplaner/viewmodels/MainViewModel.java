package org.technikum.tourplaner.viewmodels;

import org.technikum.tourplaner.models.MainModel;

public class MainViewModel {
    private final MainModel mainModel;

    public MainViewModel() {
        this.mainModel = new MainModel();
    }

    public boolean changeText(){
        return mainModel.changeText();
    }
}
