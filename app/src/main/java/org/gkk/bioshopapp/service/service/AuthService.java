package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.config.InitRootUser;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;

import java.util.List;

public interface AuthService {
    List<String> register(UserRegisterServiceModel model);

    User initRoot(InitRootUser initRootUser);
}
