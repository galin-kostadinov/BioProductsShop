package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.data.model.ShoppingCart;
import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartServiceModel;

import java.time.LocalDateTime;

public interface ShoppingCartService {
    ShoppingCartServiceModel getShoppingCartByBuyer(String buyer) throws Exception;

    ShoppingCart addProduct(String buyer, String productId, Integer quantity) throws Exception;

    ShoppingCart removeProduct(String buyer, String productId) throws Exception;

    boolean buyProducts(String buyer) throws Exception;

    void deleteExpiredShoppingCart(LocalDateTime now);

    void deleteShoppingCartByUsername(String username);

    void removeProductById(String productId);
}
