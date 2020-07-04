package org.gkk.bioshopapp.web.model.user;

import javax.validation.constraints.*;

public class UserRegisterBindingModel {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    public UserRegisterBindingModel() {
    }

    @NotBlank(message = "Username is mandatory.")
    @Size(min = 4, message = "Username must be min 4 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.]{4,}$", message = "Username may contain letters, digits, special symbols('-', '_', '.').")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be min 6 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.$#]{6,}$", message = "Password may contain letters, digits, special symbols('-', '_', '.', '$', '#').")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be min 6 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.$#]{6,}$", message = "Password may contain letters, digits, special symbols('-', '_', '.').")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotBlank(message = "Email is mandatory.")
    @Email(message="Invalid email.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
