package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final RoleService roleService;
    private final HashingService hashingService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidation userValidation, RoleService roleService, HashingService hashingService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
        this.roleService = roleService;
        this.hashingService = hashingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserProfileServiceModel getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, UserProfileServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }

    @Override
    public void editUserProfile(UserEditProfileServiceModel serviceModel) {
        User user = this.userRepository.findByUsername(serviceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        if (!userValidation.isValid(serviceModel, user.getPassword())) {
            throw new IllegalArgumentException(USERNAME_OR_PASSWORD_ARE_INCORRECT);
        }

        user.setPassword(this.hashingService.hash(serviceModel.getNewPassword()));
        this.userRepository.save(user);
    }

    @Override
    public List<UserProfileServiceModel> getAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserProfileServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserEntityByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }

    @Override
    public void makeAdmin(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        user.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void makeUser(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        user.getAuthorities().remove(this.roleService.findByAuthority("ROLE_ADMIN"));

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }
}
