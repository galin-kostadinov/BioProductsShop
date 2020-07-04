package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.user.UserLoginServiceModel;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;

import java.util.List;

public interface AuthService {
    List<String> register(UserRegisterServiceModel model);

    UserLoginServiceModel login(UserLoginServiceModel serviceModel) throws Exception;
}
