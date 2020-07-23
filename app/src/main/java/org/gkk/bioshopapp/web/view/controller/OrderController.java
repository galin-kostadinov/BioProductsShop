package org.gkk.bioshopapp.web.view.controller;

import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.web.annotation.PageTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Orders")
    public String getAllOrders(Model model, Principal principal, Pageable pageable) {
        Page<OrderServiceModel> orderServiceModels = this.orderService.getAllOrdersByUser(principal.getName(), pageable);
        model.addAttribute("orderServiceModels", orderServiceModels);
        return "order/orders";
    }
}
