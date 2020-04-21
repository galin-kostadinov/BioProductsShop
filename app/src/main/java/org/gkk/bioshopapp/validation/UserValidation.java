package org.gkk.bioshopapp.validation;

import org.gkk.bioshopapp.service.model.UserEditProfileServiceModel;

public interface UserValidation {
    boolean isValid(UserEditProfileServiceModel user);
}
