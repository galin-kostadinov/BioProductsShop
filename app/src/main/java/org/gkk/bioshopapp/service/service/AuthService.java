package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.LoginUserServiceModel;
import org.gkk.bioshopapp.service.model.RegisterUserServiceModel;

public interface AuthService {
    void register(RegisterUserServiceModel model);

    LoginUserServiceModel login(LoginUserServiceModel serviceModel) throws Exception;
}
