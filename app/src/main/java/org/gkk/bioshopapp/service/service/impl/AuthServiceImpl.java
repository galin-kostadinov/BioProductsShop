package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UsersRepository;
import org.gkk.bioshopapp.service.model.LoginUserServiceModel;
import org.gkk.bioshopapp.service.model.RegisterUserServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.validation.AuthValidation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final HashingService hashingService;
    private final AuthValidation authValidation;

    public AuthServiceImpl(
            UsersRepository usersRepository,
            ModelMapper modelMapper,
            HashingService hashingService, AuthValidation authValidation) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.hashingService = hashingService;
        this.authValidation = authValidation;
    }

    @Override
    public void register(RegisterUserServiceModel model) {
        if (!authValidation.isValid(model)) {
            return;
        }

        User user = modelMapper.map(model, User.class);
        user.setPassword(hashingService.hash(user.getPassword()));
        usersRepository.save(user);
    }

    @Override
    public LoginUserServiceModel login(LoginUserServiceModel serviceModel) throws Exception {
        String passwordHash = hashingService.hash(serviceModel.getPassword());

        return usersRepository
                .findByUsernameAndPassword(serviceModel.getUsername(), passwordHash)
                .map(user -> modelMapper.map(user, LoginUserServiceModel.class))
                .orElseThrow(() -> new Exception("Invalid user"));
    }
}