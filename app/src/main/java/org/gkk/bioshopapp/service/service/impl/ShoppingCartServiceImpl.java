package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.Product;
import org.gkk.bioshopapp.data.model.ShoppingCart;
import org.gkk.bioshopapp.data.model.ShoppingCartProduct;
import org.gkk.bioshopapp.data.repository.ShoppingCartRepository;
import org.gkk.bioshopapp.service.model.order.OrderProductCreateServiceModel;
import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartProductServiceModel;
import org.gkk.bioshopapp.service.model.shoppingcart.ShoppingCartServiceModel;
import org.gkk.bioshopapp.service.service.OrderService;
import org.gkk.bioshopapp.service.service.ProductService;
import org.gkk.bioshopapp.service.service.ShoppingCartService;
import org.gkk.bioshopapp.service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final static long EXPIRY_DAYS = 7;

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, ProductService productService, OrderService orderService, ModelMapper modelMapper) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ShoppingCartServiceModel getShoppingCartByBuyer(String buyer) throws Exception {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByBuyer(buyer).orElse(null);

        shoppingCart = updateCart(shoppingCart, buyer);

        ShoppingCartServiceModel shoppingCartServiceModel = new ShoppingCartServiceModel();
        shoppingCartServiceModel.setId(shoppingCart.getId());

        List<ShoppingCartProductServiceModel> shoppingCartProductServiceModels = shoppingCart.getShoppingCartProducts().stream().map(p -> {
            ShoppingCartProductServiceModel product = new ShoppingCartProductServiceModel();
            product.setQuantity(p.getQuantity());
            product.setProduct(productService.parseToProductDetailsModel(p.getProduct()));
            return product;
        }).collect(Collectors.toList());

        shoppingCartServiceModel.setShoppingCartProducts(shoppingCartProductServiceModels);

        return shoppingCartServiceModel;
    }

    @Override
    public ShoppingCart addProduct(String buyer, String productId, Integer quantity) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity can not be negative or zero value.");
        }

        ShoppingCart shoppingCart = this.shoppingCartRepository.findByBuyer(buyer).orElse(null);
        shoppingCart = updateCart(shoppingCart, buyer);

        ShoppingCartProduct shoppingCartProduct = shoppingCart.getShoppingCartProducts()
                .stream()
                .filter(pr -> pr.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        if (shoppingCartProduct == null) {
            Product product = this.productService.getProduct(productId);
            shoppingCartProduct = new ShoppingCartProduct();
            shoppingCartProduct.setProduct(product);
            shoppingCartProduct.setQuantity(quantity);
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCart.getShoppingCartProducts().add(shoppingCartProduct);
        } else {
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + quantity);
        }

        return shoppingCart;
    }

    @Override
    public ShoppingCart removeProduct(String buyer, String productId) throws Exception {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByBuyer(buyer).orElse(null);
        shoppingCart = updateCart(shoppingCart, buyer);

        ShoppingCartProduct shoppingCartProduct = shoppingCart.getShoppingCartProducts()
                .stream()
                .filter(pr -> pr.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        shoppingCart.getShoppingCartProducts().remove(shoppingCartProduct);
        return this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }

    @Override
    public void removeProductById(String productId) {
        List<ShoppingCart> shoppingCarts = this.shoppingCartRepository.findAllByProductId(productId);

        for (ShoppingCart shoppingCart : shoppingCarts) {
            ShoppingCartProduct shoppingCartProduct = shoppingCart.getShoppingCartProducts()
                    .stream()
                    .filter(pr -> pr.getProduct().getId().equals(productId))
                    .findFirst().orElse(null);

            shoppingCart.getShoppingCartProducts().remove(shoppingCartProduct);
            this.shoppingCartRepository.saveAndFlush(shoppingCart);
        }
    }

    @Override
    public boolean buyProducts(String buyer) throws Exception {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByBuyer(buyer).orElse(null);

        if (shoppingCart == null || shoppingCart.getShoppingCartProducts().size() == 0) {
            return false;
        }

        List<OrderProductCreateServiceModel> ordersService =
                shoppingCart.getShoppingCartProducts()
                        .stream()
                        .map(o -> this.modelMapper.map(o, OrderProductCreateServiceModel.class))
                        .collect(Collectors.toList());

        this.orderService.create(ordersService, buyer);
        this.shoppingCartRepository.delete(shoppingCart);
        return true;
    }

    @Override
    public void deleteShoppingCartByUsername(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByBuyer(username).orElse(null);

        if (shoppingCart == null) {
            return;
        }

        this.shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public void deleteExpiredShoppingCart(LocalDateTime now) {
        this.shoppingCartRepository.deleteAllByExpiryDateIsLessThanEqual(now);
    }

    private ShoppingCart createShoppingCart(String buyer) throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setBuyer(this.userService.getUserEntityByUsername(buyer));
        shoppingCart.setExpiryDate(LocalDateTime.now().plusDays(EXPIRY_DAYS));
        return this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }

    private ShoppingCart updateCart(ShoppingCart shoppingCart, String buyer) throws Exception {
        if (shoppingCart == null) {
            shoppingCart = createShoppingCart(buyer);
        } else {
            shoppingCart.setExpiryDate(LocalDateTime.now().plusDays(EXPIRY_DAYS));
            this.shoppingCartRepository.saveAndFlush(shoppingCart);
        }

        return shoppingCart;
    }
}
