package org.technikum.tourplaner.models;

@Deprecated
public class UserModel {
    private final String username;
    private final String password;

    public UserModel() {
        this.username = "username";
        this.password = "password";
    }

    public boolean isValidCredentials(String inputUsername, String inputPassword){
        return  inputUsername.equals(username) && inputPassword.equals(password);
    }
}