package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.ShoppingCart;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.ShoppingCartRepository;
import org.gkk.bioshopapp.data.repository.UserRepository;
import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.gkk.bioshopapp.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartServiceImplTest extends TestBase {

    private final static String USERNAME = UUID.randomUUID().toString();

    @MockBean
    ShoppingCartRepository shoppingCartRepository;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductService productService;

    @MockBean
    OrderService orderService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Test
    public void getShoppingCartByBuyer_ifNotProductAdded_shouldReturnCartWithEmptyProductSet() throws Exception {
        User user = new User();
        user.setUsername(USERNAME);
        user.setId(UUID.randomUUID().toString());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setBuyer(user);
        shoppingCart.setId(UUID.randomUUID().toString());
        shoppingCart.setShoppingCartProducts(new HashSet<>());
        shoppingCart.setExpiryDate(LocalDateTime.now().plusDays(3));

        Mockito.when(shoppingCartRepository.findByBuyer(USERNAME)).thenReturn(Optional.of(shoppingCart));

        ShoppingCartServiceModel shoppingCartServiceModel = shoppingCartService.getShoppingCartByBuyer(USERNAME);

        assertNotNull(shoppingCartServiceModel);
        assertEquals(shoppingCart.getId(), shoppingCartServiceModel.getId());
        assertTrue(shoppingCartServiceModel.getShoppingCartProducts().isEmpty());
    }

    @Test
    public void addProduct_ifQuantityIsNegativeValue_shouldThrow() {
        String productId = UUID.randomUUID().toString();
        int quantity = -1;

        assertThrows(IllegalArgumentException.class, () -> shoppingCartService.addProduct(USERNAME, productId, quantity));
    }

    @Test
    public void buyProducts_whenShoppingCartIsEmpty_shouldReturnFalse() throws Exception {
        String username = UUID.randomUUID().toString();
        assertFalse(shoppingCartService.buyProducts(username));
    }
}