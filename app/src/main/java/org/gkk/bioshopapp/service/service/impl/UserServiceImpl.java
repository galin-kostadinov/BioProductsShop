package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UsersRepository;
import org.gkk.bioshopapp.service.model.ProductServiceModel;
import org.gkk.bioshopapp.service.model.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final UserValidation userValidation;
    private final HashingService hashingService;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UsersRepository usersRepository, UserValidation userValidation, HashingService hashingService, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.userValidation = userValidation;
        this.hashingService = hashingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserProfileServiceModel getUserByUsername(String username) throws Exception {
        return this.usersRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, UserProfileServiceModel.class))
                .orElseThrow(() -> new Exception("Invalid user"));
    }

    @Override
    public void editUserProfile(UserEditProfileServiceModel serviceModel) throws Exception {
        serviceModel.setOldPassword(this.hashingService.hash(serviceModel.getOldPassword()));

        if (!userValidation.isValid(serviceModel)) {
            throw new Exception("Password not mach.");
        }

        User user = this.usersRepository.findByUsername(serviceModel.getUsername())
                .orElseThrow(() -> new Exception("Invalid user"));

        user.setPassword(this.hashingService.hash(serviceModel.getNewPassword()));
        this.usersRepository.save(user);
    }

    @Override
    public List<UserProfileServiceModel> getAllUsers() {
        return this.usersRepository.findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserProfileServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserEntityByUsername(String username) throws Exception {
        return this.usersRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Invalid user"));
    }
}
