package org.gkk.bioshopapp.validation;

import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;

import java.util.List;

public interface UserValidation {
    List<String> getViolations(UserEditProfileServiceModel user);
}
