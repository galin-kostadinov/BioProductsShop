package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Role;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserRepository userRepository;
    UserValidation userValidation;
    RoleService roleService;
    HashingService hashingService;
    ModelMapper modelMapper;

    UserService userService;

    @BeforeEach
    public void setupTest() {
        userRepository = Mockito.mock(UserRepository.class);
        userValidation = Mockito.mock(UserValidation.class);
        roleService = Mockito.mock(RoleService.class);
        hashingService = Mockito.mock(HashingService.class);
        modelMapper = new ModelMapper();

        userService = new UserServiceImpl(userRepository, userValidation, roleService, hashingService, modelMapper);
    }

    @Test
    public void getUserByUsername_whenUserExist_shouldReturnUser() throws Exception {
        String username = "ivanov";
        String email = "ivanov@abv.bg";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserProfileServiceModel expectedUser = this.modelMapper.map(user, UserProfileServiceModel.class);

        UserProfileServiceModel resultUser = userService.getUserByUsername(user.getUsername());

        assertEquals(expectedUser.getUsername(), resultUser.getUsername());
        assertEquals(expectedUser.getEmail(), resultUser.getEmail());
    }

    @Test
    public void getUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        String username = "ivanov";

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    public void editUserProfile_whenUserDoesNotExist_shouldThrowException() {
        String username = "ivanov";
        UserEditProfileServiceModel serviceModel = new UserEditProfileServiceModel();
        serviceModel.setUsername(username);

        assertThrows(UsernameNotFoundException.class, () -> userService.editUserProfile(serviceModel));
    }

    @Test
    public void editUserProfile_whenValidationServiceReturnFalse_shouldThrowException() {
        String username = "ivanov";
        String password = "password123A";

        UserEditProfileServiceModel serviceModel = new UserEditProfileServiceModel();
        serviceModel.setUsername(username);
        serviceModel.setOldPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Mockito.when(userRepository.findByUsername(serviceModel.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userValidation.isValid(serviceModel, user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.editUserProfile(serviceModel));
    }

    @Test
    public void editUserProfile_whenDataIsValid_shouldEditProfile() throws Exception {
        String username = "ivanov";
        String passwordOld = "password123A";
        String passwordNew = "123passwordA";

        UserEditProfileServiceModel serviceModel = new UserEditProfileServiceModel();
        serviceModel.setUsername(username);
        serviceModel.setOldPassword(passwordOld);
        serviceModel.setNewPassword(passwordNew);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordOld);

        Mockito.when(userRepository.findByUsername(serviceModel.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userValidation.isValid(serviceModel, user.getPassword())).thenReturn(true);
        Mockito.when(hashingService.hash(serviceModel.getNewPassword())).thenReturn(passwordNew);

        userService.editUserProfile(serviceModel);
        assertEquals(passwordNew, user.getPassword());
    }

    @Test
    public void getAllUsers_whenThereAreUsersInUserRepository_shouldReturnUserServiceList() throws Exception {
        String username1 = "ivanov";
        String username2 = "ivanova";

        User user1 = new User();
        user1.setUsername(username1);

        User user2 = new User();
        user1.setUsername(username2);

        List<User> users = new ArrayList<>();

        users.add(user1);
        users.add(user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserProfileServiceModel> usersDb = userService.getAllUsers();

        assertEquals(user1.getUsername(), usersDb.get(0).getUsername());
        assertEquals(user2.getUsername(), usersDb.get(1).getUsername());
    }

    @Test
    public void getAllUsers_whenThereAreNotUsersInUserRepository_shouldReturnEmptyList() throws Exception {
        List<UserProfileServiceModel> usersDb = userService.getAllUsers();

        assertEquals(0, usersDb.size());
    }

    @Test
    public void getUserEntityByUsername_whenUserExist_shouldReturnUser() throws Exception {
        String username = "ivanov";
        String email = "ivanov@abv.bg";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User resultUser = userService.getUserEntityByUsername(user.getUsername());

        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getEmail(), resultUser.getEmail());
    }

    @Test
    public void getUserEntityByUsername_whenUserDoesNotExist_shouldThrowException() {
        String username = "ivanov";

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserEntityByUsername(username));
    }

    @Test
    public void makeAdmin_whenUserWithThatIdDoesNotExist_shouldThrowException() {
        String id = "ivanov_123";

        assertThrows(UsernameNotFoundException.class, () -> userService.makeAdmin(id));
    }

    @Test
    public void makeAdmin_whenUserExist_shouldAddRoleAdmin() {
        String id = "ivanov_123";
        User user = new User();
        user.setId(id);

        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleService.findByAuthority("ROLE_ADMIN")).thenReturn(role);

        userService.makeAdmin(id);

        assertTrue(user.getAuthorities().contains(role));
    }

    @Test
    public void makeUser_whenUserWithThatIdDoesNotExist_shouldThrowException() {
        String id = "ivanov_123";

        assertThrows(UsernameNotFoundException.class, () -> userService.makeUser(id));
    }

    @Test
    public void makeUser_whenUserExist_shouldRemoveRoleAdmin() {
        String id = "ivanov_123";
        User user = new User();
        user.setId(id);

        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");

        user.getAuthorities().add(role);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleService.findByAuthority("ROLE_ADMIN")).thenReturn(role);

        assertTrue(user.getAuthorities().contains(role));

        userService.makeUser(id);

        assertFalse(user.getAuthorities().contains(role));
    }
}