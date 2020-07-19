package org.gkk.bioshopapp.service.model.shoppingcart;

import org.gkk.bioshopapp.service.model.product.ProductDetailsServiceModel;

import java.io.Serializable;

public class ShoppingCartProductServiceModel implements Serializable {

    private ProductDetailsServiceModel product;

    private Integer quantity;

    public ShoppingCartProductServiceModel() {
    }

    public ShoppingCartProductServiceModel(ProductDetailsServiceModel product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDetailsServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductDetailsServiceModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
