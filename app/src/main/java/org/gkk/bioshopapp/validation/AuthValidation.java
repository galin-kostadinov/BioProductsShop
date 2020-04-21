package org.gkk.bioshopapp.validation;


import org.gkk.bioshopapp.service.model.RegisterUserServiceModel;

public interface AuthValidation {
    boolean isValid(RegisterUserServiceModel user);
}
