package org.gkk.bioshopapp.validation;

import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;

public interface UserValidation {
    boolean isValid(UserEditProfileServiceModel userService, String oldPassword);
}
