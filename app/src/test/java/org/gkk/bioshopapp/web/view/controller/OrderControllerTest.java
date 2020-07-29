package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.service.OrderService;
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
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetAllOrders() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(orderService.getAllOrdersByUser("admin", pageable)).thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orderServiceModels"))
                .andExpect(view().name("order/orders"));
    }
}