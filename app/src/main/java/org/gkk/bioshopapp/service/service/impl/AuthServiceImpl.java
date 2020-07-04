package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<String> register(UserRegisterServiceModel model) {
        if (authValidation.getViolations(model).size() > 0) {
          return authValidation.getViolations(model);
        }

        User user = modelMapper.map(model, User.class);
        user.setPassword(hashingService.hash(user.getPassword()));

        if (this.userRepository.count() == 0) {
            user.setAuthorities(this.roleService.findAllRoles());
        } else {
            user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        userRepository.save(user);

        return null;
    }
}