package org.gkk.bioshopapp.web.view.model.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserEditProfileBindingModel {

    private String username;

    private String email;

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

    public UserEditProfileBindingModel() {
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

    @NotBlank(message = "Email is mandatory.")
    @Email(message="Invalid email.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be min 6 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.$#]{6,}$", message = "Password may contain letters, digits, special symbols('-', '_', '.').")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be min 6 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.$#]{6,}$", message = "Password may contain letters, digits, special symbols('-', '_', '.').")
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be min 6 characters long.")
    @Pattern(regexp = "^[A-Za-z0-9-_.$#]{6,}$", message = "Password may contain letters, digits, special symbols('-', '_', '.').")
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
