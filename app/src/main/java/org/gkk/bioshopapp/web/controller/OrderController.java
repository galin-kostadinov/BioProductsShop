package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.OrderServiceModel;
import org.gkk.bioshopapp.service.model.ProductServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.model.OrderProductModel;
import org.gkk.bioshopapp.web.model.ProductBuyModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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

    public OrderController(OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/orders")
    public ModelAndView getAllOrders(ModelAndView model, HttpSession session) {
        String username = session.getAttribute("username").toString();
        List<OrderServiceModel> orders = this.orderService.getAllOrdersByUser(username);

        model.addObject("orders", orders);

        return super.view("order/orders", model);
    }

    @GetMapping("/cart")
    public ModelAndView getShoppingCart(ModelAndView model, HttpSession session) {
        HashMap<String, OrderProductModel> cartInSession =
                (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        List<OrderProductModel> cart = new ArrayList<>(cartInSession.values());

        BigDecimal cartTotalPrice = cart.stream()
                .map(op -> op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addObject("cart", cart);
        model.addObject("cartTotalPrice", cartTotalPrice);
        model.setViewName("order/shopping-cart");

        return model;
    }

    @PostMapping("/buy")
    public ModelAndView buy(ModelAndView model, HttpSession session) throws Exception {
        String username = session.getAttribute("username").toString();
        HashMap<String, OrderProductModel> cartInSession =
                (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        if (cartInSession.isEmpty()) {
            return super.redirect("/order/cart");
        }

        List<OrderProductServiceModel> ordersService =
                cartInSession.values()
                        .stream()
                        .map(o -> this.modelMapper.map(o, OrderProductServiceModel.class))
                        .collect(Collectors.toList());

        this.orderService.create(ordersService, username);

        cartInSession.clear();

        return super.redirect("/order/orders");
    }

    @PostMapping("/add-to-cart/{id}")
    public ModelAndView addToCart(@PathVariable String id, Integer quantity, HttpSession session) throws Exception {
        HashMap<String, OrderProductModel> cart = (HashMap<String, OrderProductModel>) session.getAttribute("cart");

        if (!cart.containsKey(id)) {
            ProductBuyModel product = this.modelMapper.map(this.productService.getProductById(id), ProductBuyModel.class);
            cart.put(id, new OrderProductModel(product, quantity));
        } else {
            OrderProductModel orderProductModel = cart.get(id);
            orderProductModel.setQuantity(orderProductModel.getQuantity() + quantity);
        }

        return super.redirect("/product");
    }

    @PostMapping("/remove-from-cart/{id}")
    public ModelAndView removeFromCart(@PathVariable String id, HttpSession session) {
        HashMap<String, OrderProductModel> cart = (HashMap<String, OrderProductModel>) session.getAttribute("cart");
        cart.remove(id);

        return super.redirect("/order/cart");
    }
}
