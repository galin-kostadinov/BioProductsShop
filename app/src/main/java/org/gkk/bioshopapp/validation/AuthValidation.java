package org.gkk.bioshopapp.validation;


import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;

public interface AuthValidation {
    boolean isValid(UserRegisterServiceModel user);
}
