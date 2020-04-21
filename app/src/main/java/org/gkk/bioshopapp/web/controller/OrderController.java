package org.gkk.bioshopapp.web.controller;

import org.gkk.bioshopapp.data.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderController(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all-orders")
    public ModelAndView getAllOrders() {
        return super.view("order/all-orders");
    }



}
