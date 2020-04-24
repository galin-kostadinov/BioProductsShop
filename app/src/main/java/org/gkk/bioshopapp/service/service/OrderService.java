package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.OrderServiceModel;

import java.util.List;

public interface OrderService {
    List<OrderServiceModel> getAllOrders();

    void create(List<OrderProductServiceModel> orderProducts, String username) throws Exception;

    void update(OrderServiceModel order);

    List<OrderServiceModel> getAllOrdersByUser(String username);
}
