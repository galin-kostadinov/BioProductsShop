package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.UserProfileServiceModel;

import java.util.List;

public interface UserService {
    UserProfileServiceModel getUserByUsername(String username) throws Exception;

    void editUserProfile(UserEditProfileServiceModel serviceModel) throws Exception;

    List<UserProfileServiceModel> getAllUsers();
}
