package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartServiceModel;
import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/carts")
public class ShoppingCartController extends BaseController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Shopping Cart")
    public String getShoppingCart(Model model, Principal principal) throws Exception {
        ShoppingCartServiceModel shoppingCartServiceModel = this.shoppingCartService.getShoppingCartByBuyer(principal.getName());
        model.addAttribute("cart", shoppingCartServiceModel);
        return "shoppingcart/shopping-cart";
    }

    @PostMapping("/add-to-cart/{id}")
    @PreAuthorize("isAuthenticated()")
    public String addToCart(@PathVariable(name = "id") String productId, Integer quantity, Principal principal) throws Exception {
        this.shoppingCartService.addProduct(principal.getName(), productId, quantity);
        return super.redirectStr("/products");
    }

    @PostMapping("/remove-from-cart/{id}")
    @PreAuthorize("isAuthenticated()")
    public String removeFromCart(@PathVariable(name = "id") String productId, Principal principal) throws Exception {
        this.shoppingCartService.removeProduct(principal.getName(), productId);
        return super.redirectStr("/carts");
    }

    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public String buy(Principal principal) throws Exception {
        if (this.shoppingCartService.buyProducts(principal.getName())) {
            return super.redirectStr("/orders?size=1&sort=dateCreated,desc&page=0");
        } else {
            return super.redirectStr("/carts");
        }
    }
}
