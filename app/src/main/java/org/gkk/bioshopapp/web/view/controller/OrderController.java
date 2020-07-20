package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"/", ""})
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Orders")
    public String getAllOrders(Model model, Principal principal) {
        List<OrderServiceModel> orders = this.orderService.getAllOrdersByUser(principal.getName());
        model.addAttribute("orders", orders);
        return "order/orders";
    }
}
