package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.service.AuthService;
import org.gkk.bioshopapp.web.view.model.user.UserRegisterBindingModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRegisterForm() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("userRegisterBindingModel"));
    }

    @Test
    public void testRegisterConfirm_whenParametersAreValid() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername(UUID.randomUUID().toString());
        String password = UUID.randomUUID().toString();
        userRegisterBindingModel.setPassword(password);
        userRegisterBindingModel.setConfirmPassword(password);
        userRegisterBindingModel.setEmail(UUID.randomUUID().toString() + "@abv.bg");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testRegisterConfirm_whenParametersAreInvalid() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel))
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("userRegisterBindingModel", "org.springframework.validation.BindingResult.userRegisterBindingModel"))
                .andExpect(view().name("redirect:register"));
    }

    @Test
    public void testGetLoginForm() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }
}