package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.springframework.stereotype.Component;

@Component
public class AuthValidationImpl implements AuthValidation {
    private final UserRepository userRepository;

    public AuthValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserRegisterServiceModel user) {
        return this.isEmailValid(user.getEmail()) &&
                this.arePasswordsValid(user.getPassword(), user.getConfirmPassword()) &&
                this.isUsernameFree(user.getUsername());
    }

    private boolean arePasswordsValid(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords don't match!");
        }

        return true;
    }

    private boolean isUsernameFree(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(String.format("User with Username: %s already exist!", username));
        }

        return true;
    }

    private boolean isEmailValid(String email) {
        //todo
        return true;
    }
}
