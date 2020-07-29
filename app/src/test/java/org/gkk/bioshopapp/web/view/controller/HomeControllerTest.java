package org.gkk.bioshopapp.web.view.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndex() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN", "ROOT"})
    public void testHome() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testContacts() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));
    }
}