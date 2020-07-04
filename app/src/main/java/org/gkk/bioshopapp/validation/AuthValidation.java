package org.gkk.bioshopapp.validation;


import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;

import java.util.List;

public interface AuthValidation {
    List<String> getViolations(UserRegisterServiceModel user);
}
