package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.springframework.stereotype.Component;

@Component
public class UserValidationImpl implements UserValidation {
    private final HashingService hashingService;

    public UserValidationImpl(HashingService hashingService) {
        this.hashingService = hashingService;
    }

    @Override
    public boolean isValid(UserEditProfileServiceModel userService, String oldPassword) {
        return this.areNewPasswordsValid(userService.getNewPassword(), userService.getConfirmNewPassword()) &&
                this.areOldPasswordValid(userService.getOldPassword(), oldPassword);
    }

    private boolean areNewPasswordsValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean areOldPasswordValid(String inputPassword, String oldPassword) {
        return this.hashingService.isPasswordMatch(inputPassword, oldPassword);
    }
}
