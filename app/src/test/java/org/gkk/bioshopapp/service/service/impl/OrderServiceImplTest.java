package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.Order;
import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.data.model.User;
import org.gkk.bioshopapp.data.repository.OrderRepository;
import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.gkk.bioshopapp.service.model.product.ProductIdServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceImplTest extends TestBase {

    private final static String USERNAME = UUID.randomUUID().toString();
    private final static int PAGE = 0;
    private final static int SIZE = 20;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserService userService;

    @MockBean
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Test
    public void create_whenOrderProductsExist_shouldCreateOrderWithOrderedProducts() throws Exception {
        String productFirstId = UUID.randomUUID().toString();
        Integer productFirstQuantity = 10;

        OrderProductCreateServiceModel orderProductFirst = new OrderProductCreateServiceModel();
        ProductIdServiceModel productFirstServiceModel = new ProductIdServiceModel();
        productFirstServiceModel.setId(productFirstId);
        orderProductFirst.setProduct(productFirstServiceModel);
        orderProductFirst.setQuantity(productFirstQuantity);

        List<OrderProductCreateServiceModel> orderProducts = new ArrayList<>();
        orderProducts.add(orderProductFirst);

        Product product = new Product();
        product.setId(productFirstId);
        Mockito.when(productService.getProduct(productFirstId)).thenReturn(product);

        orderService.create(orderProducts, USERNAME);

        ArgumentCaptor<Order> argumentOrder = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository).saveAndFlush(argumentOrder.capture());

        Order order = argumentOrder.getValue();

        assertEquals(productFirstId, order.getOrderProducts().get(0).getProduct().getId());
        assertEquals(productFirstQuantity, order.getOrderProducts().get(0).getQuantity());
    }

    @Test
    public void create_whenBuyerExist_shouldCreateOrderWithGivenBuyer() throws Exception {
        User buyer = new User();
        buyer.setUsername(USERNAME);

        Mockito.when(userService.getUserEntityByUsername(USERNAME)).thenReturn(buyer);

        orderService.create(new ArrayList<>(), USERNAME);

        ArgumentCaptor<Order> argumentOrder = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository).saveAndFlush(argumentOrder.capture());

        Order order = argumentOrder.getValue();

        assertEquals(order.getBuyer().getUsername(), buyer.getUsername());
    }

    @Test
    public void getAllOrdersByUser_whenBuyerExist_shouldReturnAllOrders() {

        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setId(String.format("order_%d", i));
            orders.add(order);
        }

        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by("id"));
        Mockito.when(this.orderRepository.findAllByUsername(USERNAME, pageable)).thenReturn(new PageImpl<>(orders, pageable, orders.size()));

        Page<OrderServiceModel> pageResult = orderService.getAllOrdersByUser(USERNAME, pageable);

        assertEquals(10, pageResult.getTotalElements());

        int index = 0;
        for (OrderServiceModel currOrder : pageResult) {
            assertEquals(orders.get(index).getId(), currOrder.getId());
            index++;
        }
    }

    @Test
    public void getAllOrdersByUser_whenBuyerDoesNotExist_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(PAGE, SIZE);

        Page<OrderServiceModel> page = orderService.getAllOrdersByUser(USERNAME, pageable);

        Assertions.assertEquals(0, page.getTotalElements());
    }
}