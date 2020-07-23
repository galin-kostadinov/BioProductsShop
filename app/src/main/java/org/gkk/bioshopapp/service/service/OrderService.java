package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.order.OrderServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void create(List<OrderProductCreateServiceModel> orderProducts, String username) throws Exception;

    Page<OrderServiceModel> getAllOrdersByUser(String username, Pageable pageable);
}
