package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartServiceModel;
import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testGetShoppingCart() throws Exception {
        ShoppingCartServiceModel shoppingCartServiceModel = new ShoppingCartServiceModel();
        shoppingCartServiceModel.setId(UUID.randomUUID().toString());
        shoppingCartServiceModel.setShoppingCartProducts(new ArrayList<>());
        Mockito.when(shoppingCartService.getShoppingCartByBuyer("admin")).thenReturn(shoppingCartServiceModel);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/carts"))
                .andExpect(status().isOk())
                .andExpect(view().name("shoppingcart/shopping-cart"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testAddToCart() throws Exception {
        String productId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/carts/add-to-cart/" + productId)
                        .param("id", productId)
                        .param("quantity", "10"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testRemoveFromCart() throws Exception {
        String productId = UUID.randomUUID().toString();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/carts/remove-from-cart/" + productId)
                        .param("id", productId))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/carts"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testBuy_WhenShoppingCartIsNotEmpty() throws Exception {
        Mockito.when(shoppingCartService.buyProducts("admin")).thenReturn(true);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/carts/buy"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/orders?size=1&sort=dateCreated,desc&page=0"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testBuy_WhenShoppingCartIsEmpty() throws Exception {
        Mockito.when(shoppingCartService.buyProducts("admin")).thenReturn(false);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/carts/buy"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/carts"));
    }
}