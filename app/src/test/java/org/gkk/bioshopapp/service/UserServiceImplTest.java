package org.gkk.bioshopapp.service;


import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.Role;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserEditProfileServiceModel;
import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.validation.UserValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest extends TestBase {

    private final static String USERNAME = UUID.randomUUID().toString();

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserValidation userValidation;

    @MockBean
    RoleService roleService;

    @MockBean
    HashingService hashingService;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Test
    public void getUserByUsername_whenUserExist_shouldReturnUser() throws Exception {
        String email = "ivanov@abv.bg";

        User user = new User();
        user.setUsername(USERNAME);
        user.setEmail(email);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserProfileServiceModel resultUser = userService.getUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getEmail(), resultUser.getEmail());
    }

    @Test
    public void getUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(USERNAME));
    }

    @Test
    public void editUserProfile_whenUserDoesNotExist_shouldThrowException() {
        UserEditProfileServiceModel serviceModel = new UserEditProfileServiceModel();
        serviceModel.setUsername(USERNAME);

        assertThrows(UsernameNotFoundException.class, () -> userService.editUserProfile(serviceModel));
    }

    @Test
    public void editUserProfile_whenDataIsValid_shouldEditProfile() {
        String passwordOld = UUID.randomUUID().toString();
        String passwordNew = UUID.randomUUID().toString();

        UserEditProfileServiceModel serviceModel = new UserEditProfileServiceModel();
        serviceModel.setUsername(USERNAME);
        serviceModel.setOldPassword(passwordOld);
        serviceModel.setNewPassword(passwordNew);

        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(passwordOld);

        Mockito.when(userRepository.findByUsername(serviceModel.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userValidation.getViolations(serviceModel)).thenReturn(new ArrayList<>());
        Mockito.when(hashingService.hash(serviceModel.getNewPassword())).thenReturn(passwordNew);

        userService.editUserProfile(serviceModel);
        assertEquals(passwordNew, user.getPassword());
    }

    @Test
    public void getAllUsers_whenThereAreUsersInUserRepository_shouldReturnUserServiceList() {
        User user1 = new User();
        user1.setUsername(USERNAME);

        User user2 = new User();
        user2.setUsername(USERNAME + "_2");

        List<User> users = new ArrayList<>();

        users.add(user1);
        users.add(user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserProfileServiceModel> usersDb = userService.getAllUsers();

        assertEquals(user1.getUsername(), usersDb.get(0).getUsername());
        assertEquals(user2.getUsername(), usersDb.get(1).getUsername());
    }

    @Test
    public void getAllUsers_whenThereAreNotUsersInUserRepository_shouldReturnEmptyList() {
        List<UserProfileServiceModel> usersDb = userService.getAllUsers();

        assertEquals(0, usersDb.size());
    }

    @Test
    public void getUserEntityByUsername_whenUserExist_shouldReturnUser() throws Exception {
        String email = "ivanov@abv.bg";

        User user = new User();
        user.setUsername(USERNAME);
        user.setEmail(email);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User resultUser = userService.getUserEntityByUsername(user.getUsername());

        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getEmail(), resultUser.getEmail());
    }

    @Test
    public void getUserEntityByUsername_whenUserDoesNotExist_shouldThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserEntityByUsername(USERNAME));
    }

    @Test
    public void makeAdmin_whenUserWithThatIdDoesNotExist_shouldThrowException() {
        String id = UUID.randomUUID().toString();

        assertThrows(UsernameNotFoundException.class, () -> userService.makeAdmin(id));
    }

    @Test
    public void makeAdmin_whenUserExist_shouldAddRoleAdmin() {
        String id = UUID.randomUUID().toString();
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
        String id = UUID.randomUUID().toString();

        assertThrows(UsernameNotFoundException.class, () -> userService.makeUser(id));
    }

    @Test
    public void makeUser_whenUserExist_shouldRemoveRoleAdmin() {
        String id = UUID.randomUUID().toString();
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