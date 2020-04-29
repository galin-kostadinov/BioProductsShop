package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.data.repository.UsersRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.springframework.stereotype.Component;

@Component
public class AuthValidationImpl implements AuthValidation {
    private final UsersRepository usersRepository;

    public AuthValidationImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean isValid(UserRegisterServiceModel user) {
        return this.isEmailValid(user.getEmail()) &&
                this.arePasswordsValid(user.getPassword(), user.getConfirmPassword()) &&
                this.isUsernameFree(user.getUsername());
    }

    private boolean arePasswordsValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean isUsernameFree(String username) {
        return !usersRepository.existsByUsername(username);
    }

    private boolean isEmailValid(String email) {
        //todo
        return true;
    }
}
