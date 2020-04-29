package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.user.UserLoginServiceModel;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;

public interface AuthService {
    void register(UserRegisterServiceModel model);

    UserLoginServiceModel login(UserLoginServiceModel serviceModel) throws Exception;
}
