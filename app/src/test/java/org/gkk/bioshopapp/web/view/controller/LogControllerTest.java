package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.service.LogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LogControllerTest {

    @MockBean
    private LogService logService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllLogs() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(logService.getPaginated(pageable)).thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/logs"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("logServiceModels"))
                .andExpect(view().name("log/logs"));
    }
}