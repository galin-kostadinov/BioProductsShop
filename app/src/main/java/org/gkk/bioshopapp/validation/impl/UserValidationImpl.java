package org.gkk.bioshopapp.validation.impl;

import org.gkk.bioshopapp.constant.ErrorMessageConstant;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@Component
public class UserValidationImpl implements UserValidation {

    private final HashingService hashingService;
    private final UserRepository userRepository;

    public UserValidationImpl(HashingService hashingService, UserRepository userRepository) {
        this.hashingService = hashingService;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> getViolations(UserEditProfileServiceModel user) {
        List<String> violations = new ArrayList<>();

        this.areNewPasswordsValid(user, violations);
        this.isUserExist(user, violations);

        return violations;
    }

    private void areNewPasswordsValid(UserEditProfileServiceModel user, List<String> violations) {
        if (!user.getNewPassword().equals(user.getConfirmNewPassword())) {
            violations.add("'New Password' and 'Confirm New Password' don't match!");
        }
    }

    private void isUserExist(UserEditProfileServiceModel user, List<String> violations) {
        User userDB = userRepository.findByUsername(user.getUsername()).orElse(null);

        if (userDB == null) {
            violations.add(USERNAME_OR_PASSWORD_ARE_INCORRECT);
            return;
        }

        if (!this.hashingService.isPasswordMatch(user.getOldPassword(), userDB.getPassword())) {
            violations.add(USERNAME_OR_PASSWORD_ARE_INCORRECT);
        }
    }
}
