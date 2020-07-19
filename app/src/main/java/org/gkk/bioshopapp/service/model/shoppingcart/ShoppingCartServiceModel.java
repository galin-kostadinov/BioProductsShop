package org.gkk.bioshopapp.service.model.shoppingcart;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartServiceModel{

    private String id;

    private List<ShoppingCartProductServiceModel> shoppingCartProducts;

    public ShoppingCartServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShoppingCartProductServiceModel> getShoppingCartProducts() {
        return shoppingCartProducts;
    }

    public void setShoppingCartProducts(List<ShoppingCartProductServiceModel> shoppingCartProducts) {
        this.shoppingCartProducts = shoppingCartProducts;
    }

    public BigDecimal calculateTotalCartPrice() {
        return this.shoppingCartProducts.stream()
                .map(op -> {
                            BigDecimal resultPrice;

                            if (op.getProduct().getPromotionalPrice() == null) {
                                resultPrice = op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity()));
                            } else {
                                resultPrice = op.getProduct().getPromotionalPrice().multiply(BigDecimal.valueOf(op.getQuantity()));
                            }

                            return resultPrice;
                        }
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
