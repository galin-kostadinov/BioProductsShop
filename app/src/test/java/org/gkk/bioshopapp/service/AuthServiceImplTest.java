package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.user.UserRegisterServiceModel;
import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.service.service.HashingService;
import org.gkk.bioshopapp.service.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest extends TestBase {

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleService roleService;

    @Autowired
    HashingService hashingService;

    @Autowired
    AuthService authService;

    @Test
    public void register_whenUserDataIsValid_shouldSaveTheUser() {
        String email = "ivanov@abv.bg";
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();

        UserRegisterServiceModel userRegisterServiceModel = new UserRegisterServiceModel();
        userRegisterServiceModel.setUsername(username);
        userRegisterServiceModel.setEmail(email);
        userRegisterServiceModel.setPassword(password);
        userRegisterServiceModel.setConfirmPassword(password);

        assertNull(authService.register(userRegisterServiceModel));

        ArgumentCaptor<User> argumentUser = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(argumentUser.capture());

        User userDB = argumentUser.getValue();

        assertNotNull(userDB);
        assertEquals(userRegisterServiceModel.getUsername(), userDB.getUsername());
        assertEquals(userRegisterServiceModel.getEmail(), userDB.getEmail());
        assertTrue(hashingService.isPasswordMatch(userRegisterServiceModel.getPassword(),userDB.getPassword() ));
    }

    @Test
    public void register_whenUserDataIsInvalid_shouldReturnViolations() {
        String email = "ivanov@abv.bg";
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();

        UserRegisterServiceModel userRegisterServiceModel = new UserRegisterServiceModel();
        userRegisterServiceModel.setUsername(username);
        userRegisterServiceModel.setEmail(email);
        userRegisterServiceModel.setPassword(password);
        userRegisterServiceModel.setConfirmPassword(UUID.randomUUID().toString());

        List<String> violations = authService.register(userRegisterServiceModel);
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }
}