package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserProfileServiceModel getUserByUsername(String username) throws Exception;

    void editUserProfile(UserEditProfileServiceModel serviceModel) throws Exception;

    List<UserProfileServiceModel> getAllUsers();

    User getUserEntityByUsername(String username) throws Exception;

    void makeAdmin(String id);

    void makeUser(String id);
}
