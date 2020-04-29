package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderProductServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderServiceModel;

import java.util.List;

public interface OrderService {
    void create(List<OrderProductCreateServiceModel> orderProducts, String username) throws Exception;

    List<OrderServiceModel> getAllOrdersByUser(String username);
}
