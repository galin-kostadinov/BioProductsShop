package org.gkk.bioshopapp.task;

import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ShoppingCartPurgeTask {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartPurgeTask(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Scheduled(cron = "${bioshopapp.cart.default.purge.cron.expression}")
    public void purgeExpired() {
        this.shoppingCartService.deleteExpiredShoppingCart(LocalDateTime.now());
    }
}
