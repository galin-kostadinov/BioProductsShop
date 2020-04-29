package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;

import java.util.List;

public interface UserService {
    UserProfileServiceModel getUserByUsername(String username) throws Exception;

    void editUserProfile(UserEditProfileServiceModel serviceModel) throws Exception;

    List<UserProfileServiceModel> getAllUsers();

    User getUserEntityByUsername(String username) throws Exception;
}
