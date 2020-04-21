package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.OrderCreateServiceModel;
import org.gkk.bioshopapp.service.model.OrderServiceModel;

import java.util.List;

public interface OrderService {
    List<OrderServiceModel> getAllOrders();

    OrderServiceModel create(OrderCreateServiceModel order);

    void update(OrderServiceModel order);
}
