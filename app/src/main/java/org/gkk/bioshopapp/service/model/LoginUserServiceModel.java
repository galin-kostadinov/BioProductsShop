package org.gkk.bioshopapp.service.model;

public class LoginUserServiceModel {
    private String username;

    private String password;

    public LoginUserServiceModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
