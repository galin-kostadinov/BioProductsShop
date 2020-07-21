package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.config.InitRootUser;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public User initRoot(InitRootUser initRootUser) {
        if (this.userRepository.count() != 0) {
            return null;
        }

        User user = new User();
        user.setUsername(initRootUser.getUsername());
        user.setEmail(initRootUser.getEmail());
        user.setPassword(hashingService.hash(initRootUser.getPassword()));
        user.setAuthorities(this.roleService.findAllRoles());

        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<String> register(UserRegisterServiceModel model) {
        List<String> violations = authValidation.getViolations(model);

        if (violations.size() > 0) {
            return violations;
        }

        User user = modelMapper.map(model, User.class);
        user.setPassword(hashingService.hash(user.getPassword()));
        user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));

        userRepository.save(user);

        return null;
    }
}