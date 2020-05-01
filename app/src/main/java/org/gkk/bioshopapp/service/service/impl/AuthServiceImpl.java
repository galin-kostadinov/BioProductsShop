package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.error.UserNotFoundException;
import org.gkk.bioshopapp.service.model.user.UserLoginServiceModel;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.USERNAME_OR_PASSWORD_ARE_INCORRECT;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final HashingService hashingService;
    private final AuthValidation authValidation;

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository,
            RoleService roleService, ModelMapper modelMapper,
            HashingService hashingService, AuthValidation authValidation) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.hashingService = hashingService;
        this.authValidation = authValidation;
    }

    @Override
    public void register(UserRegisterServiceModel model) {
        this.roleService.seedRolesInDb();

        if (!authValidation.isValid(model)) {
            return;
        }

        User user = modelMapper.map(model, User.class);
        user.setPassword(hashingService.hash(user.getPassword()));

        if (this.userRepository.count() == 0) {
            user.setAuthorities(this.roleService.findAllRoles());
        } else {
            user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        userRepository.save(user);
    }

    @Override
    public UserLoginServiceModel login(UserLoginServiceModel serviceModel){
        String passwordHash = hashingService.hash(serviceModel.getPassword());

        return userRepository
                .findByUsernameAndPassword(serviceModel.getUsername(), passwordHash)
                .map(user -> modelMapper.map(user, UserLoginServiceModel.class))
                .orElseThrow(() -> new UserNotFoundException(USERNAME_OR_PASSWORD_ARE_INCORRECT));
    }
}