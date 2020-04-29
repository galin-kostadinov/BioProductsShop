package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.data.repository.UsersRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.validation.UserValidation;
import org.springframework.stereotype.Component;

@Component
public class UserValidationImpl implements UserValidation {
    private final UsersRepository usersRepository;

    public UserValidationImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean isValid(UserEditProfileServiceModel user) {
        return this.areNewPasswordsValid(user.getNewPassword(), user.getConfirmNewPassword()) &&
                this.areOldPasswordValid(user.getUsername(), user.getOldPassword());
    }

    private boolean areNewPasswordsValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean areOldPasswordValid(String username, String password) {
        return usersRepository.findByUsernameAndPassword(username, password).isPresent();
    }
}
