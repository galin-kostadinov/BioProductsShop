package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.user.UserProfileServiceModel;
import org.gkk.bioshopapp.service.service.UserService;
import org.gkk.bioshopapp.web.view.model.user.UserEditProfileBindingModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT"})
    public void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/all-users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetProfile() throws Exception {
        UserProfileServiceModel userProfileServiceModel = new UserProfileServiceModel();

        Mockito.when(userService.getUserByUsername("admin")).thenReturn(userProfileServiceModel);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attributeExists("model"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetEditProfile() throws Exception {
        UserProfileServiceModel userProfileServiceModel = new UserProfileServiceModel();

        Mockito.when(userService.getUserByUsername("admin")).thenReturn(userProfileServiceModel);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit-profile"))
                .andExpect(model().attributeExists("userEditProfileBindingModel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testEditUserConfirm_whenParametersAreValid() throws Exception {
        UserEditProfileBindingModel userEditProfileBindingModel = new UserEditProfileBindingModel();
        userEditProfileBindingModel.setUsername(UUID.randomUUID().toString());
        String password = UUID.randomUUID().toString();
        userEditProfileBindingModel.setEmail(UUID.randomUUID().toString() + "@abv.bg");
        userEditProfileBindingModel.setOldPassword(password);
        userEditProfileBindingModel.setNewPassword(password);
        userEditProfileBindingModel.setConfirmNewPassword(password);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/profile/edit")
                        .flashAttr("userEditProfileBindingModel", userEditProfileBindingModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/profile"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testEditUserConfirm_whenParametersAreInvalid() throws Exception {
        UserEditProfileBindingModel userEditProfileBindingModel = new UserEditProfileBindingModel();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/profile/edit")
                        .flashAttr("userEditProfileBindingModel", userEditProfileBindingModel))
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("userEditProfileBindingModel", "org.springframework.validation.BindingResult.userEditProfileBindingModel"))
                .andExpect(view().name("redirect:/users/profile/edit"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetDeleteProfile() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/profile/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/delete-profile"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testDeleteProfileConfirm() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/profile/delete"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT"})
    public void testSetAdminRole() throws Exception {
        String userId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set-admin/" + userId))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROOT"})
    public void testSetUserRole() throws Exception {
        String userId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set-user/" + userId))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users"));
    }
}