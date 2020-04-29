package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.web.model.order.OrderProductModel;
import org.gkk.bioshopapp.web.model.product.ProductShoppingCartModel;
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

        BigDecimal cartTotalPrice = calculateTotalCartPrice(cart);

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
    public ModelAndView addToCart(@PathVariable String id, Integer quantity, HttpSession session){
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
