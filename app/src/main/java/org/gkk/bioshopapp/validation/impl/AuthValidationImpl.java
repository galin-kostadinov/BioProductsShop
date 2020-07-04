package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthValidationImpl implements AuthValidation {
    private final UserRepository userRepository;

    public AuthValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<String> getViolations(UserRegisterServiceModel user) {
        List<String> violations = new ArrayList<>();

        this.isEmailValid(user.getEmail(), violations);
        this.arePasswordsValid(user.getPassword(), user.getConfirmPassword(), violations);
        this.isUsernameFree(user.getUsername(), violations);

        return violations;
    }

    private void arePasswordsValid(String password, String confirmPassword, List<String> violations) {
        if (!password.equals(confirmPassword)) {
            violations.add("Passwords don't match!");
        }
    }

    private void isUsernameFree(String username, List<String> violations) {
        if (userRepository.existsByUsername(username)) {
            violations.add(String.format("User with Username [%s] already exist!", username));
        }
    }

    private void isEmailValid(String email, List<String> violations) {
        if (userRepository.existsByEmail(email)) {
            violations.add(String.format("User with Email [%s] already exist!", email));
        }
    }
}
