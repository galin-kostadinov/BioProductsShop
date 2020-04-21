package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Order;
import org.gkk.bioshopapp.data.repository.OrderRepository;
import org.gkk.bioshopapp.service.model.OrderCreateServiceModel;
import org.gkk.bioshopapp.service.model.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel create(OrderCreateServiceModel orderService) {
        Order order = this.modelMapper.map(orderService, Order.class);
        order.setDateCreated(LocalDateTime.now());

        return this.modelMapper.map(this.orderRepository.save(order), OrderServiceModel.class);
    }

    @Override
    public void update(OrderServiceModel orderService) {
        Order order = this.modelMapper.map(orderService, Order.class);
        this.orderRepository.save(order);
    }
}
