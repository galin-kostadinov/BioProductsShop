package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.error.PriceHishoryNotFoundException;
import org.gkk.bioshopapp.error.ProductNotFoundException;
import org.gkk.bioshopapp.error.UserNotFoundException;
import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.gkk.bioshopapp.web.model.order.OrderProductModel;
import org.gkk.bioshopapp.web.model.product.ProductShoppingCartModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Orders")
    public ModelAndView getAllOrders(ModelAndView model, Principal principal) {
        String username = principal.getName();
        List<OrderServiceModel> orders = this.orderService.getAllOrdersByUser(username);

        model.addObject("orders", orders);

        return super.view("order/orders", model);
    }

    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Shopping Cart")
    public ModelAndView getShoppingCart(ModelAndView model, HttpSession session) {
        HashMap<String, OrderProductModel> cartInSession =
                (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        List<OrderProductModel> cart = new ArrayList<>(cartInSession.values());

        BigDecimal cartTotalPrice = calculateTotalCartPrice(cart);

        model.addObject("cart", cart);
        model.addObject("cartTotalPrice", cartTotalPrice);
        model.setViewName("order/shopping-cart");

        return model;
    }

    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView buy(HttpSession session) throws Exception {
        String username = session.getAttribute("username").toString();
        HashMap<String, OrderProductModel> cartInSession =
                (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        if (cartInSession.isEmpty()) {
            return super.redirect("/order/cart");
        }

        List<OrderProductCreateServiceModel> ordersService =
                cartInSession.values()
                        .stream()
                        .map(o -> this.modelMapper.map(o, OrderProductCreateServiceModel.class))
                        .collect(Collectors.toList());

        this.orderService.create(ordersService, username);

        cartInSession.clear();

        return super.redirect("/order/orders");
    }

    @PostMapping("/add-to-cart/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCart(@PathVariable String id, Integer quantity, HttpSession session) {
        HashMap<String, OrderProductModel> cart = (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        if (!cart.containsKey(id)) {
            ProductShoppingCartModel product =
                    this.modelMapper.map(this.productService.getShoppingCartProductModelById(id),
                            ProductShoppingCartModel.class);
            cart.put(id, new OrderProductModel(product, quantity));
        } else {
            OrderProductModel orderProductModel = cart.get(id);
            orderProductModel.setQuantity(orderProductModel.getQuantity() + quantity);
        }

        return super.redirect("/product");
    }

    @PostMapping("/remove-from-cart/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeFromCart(@PathVariable String id, HttpSession session) {
        HashMap<String, OrderProductModel> cart = (HashMap<String, OrderProductModel>) session.getAttribute("cart");
        cart.remove(id);

        return super.redirect("/order/cart");
    }

    private BigDecimal calculateTotalCartPrice(List<OrderProductModel> cart) {
        return cart.stream()
                .map(op -> {
                            BigDecimal resultPrice;

                            if (op.getProduct().getPromotionalPrice() == null) {
                                resultPrice = op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity()));
                            } else {
                                resultPrice = op.getProduct().getPromotionalPrice().multiply(BigDecimal.valueOf(op.getQuantity()));
                            }

                            return resultPrice;
                        }
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
