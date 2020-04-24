package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.*;
import org.gkk.bioshopapp.data.repository.OrderRepository;
import org.gkk.bioshopapp.service.model.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.OrderServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, ProductService productService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
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
    public void create(List<OrderProductServiceModel> orderProducts, String username) throws Exception {
        Order order = new Order();
        order.setDateCreated(LocalDateTime.now());
        order.setStatus(OrderStatus.PAID);
        User buyer = this.userService.getUserEntityByUsername(username);
        order.setBuyer(buyer);

        List<OrderProduct> orderProductList =
                orderProducts.stream()
                        .map(o -> {
                            OrderProduct orderProduct = new OrderProduct();
                            try {
                                Product product = this.productService.getProduct(o.getProduct().getId());
                                orderProduct.setProduct(product);
                                orderProduct.setQuantity(o.getQuantity());
                                orderProduct.setOrder(order);
                            } catch (Exception e) {
                                ;
                            }

                            return orderProduct;
                        })
                        .collect(Collectors.toList());
        order.setOrderProducts(orderProductList);

        this.orderRepository.saveAndFlush(order);
    }

    @Override
    public void update(OrderServiceModel orderService) {
        Order order = this.modelMapper.map(orderService, Order.class);
        this.orderRepository.save(order);
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByUser(String username) {
        return this.orderRepository.findAllByUsername(username)
                .stream()
                .map(order -> {
                    OrderServiceModel orderServiceModel = this.modelMapper.map(order, OrderServiceModel.class);
                    orderServiceModel.setTotalPrice(order.getTotalOrderPrice());
                    return orderServiceModel;
                })
                .collect(Collectors.toList());
    }
}
