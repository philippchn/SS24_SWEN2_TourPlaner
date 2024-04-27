package org.technikum.tourplaner.viewmodels;

import org.technikum.tourplaner.models.UserModel;

public class LoginViewModel {
    private final UserModel userModel;

    public LoginViewModel() {
        this.userModel = new UserModel();
    }

    public boolean checkLogin(String inputUsername, String inputPassword){
        return userModel.isValidCredentials(inputUsername, inputPassword);
    }
}
