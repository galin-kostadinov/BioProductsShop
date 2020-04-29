package org.gkk.bioshopapp.web.model.order;

import org.gkk.bioshopapp.web.model.product.ProductShoppingCartModel;

import java.io.Serializable;

public class OrderProductModel implements Serializable {

    private ProductShoppingCartModel product;

    private Integer quantity;

    public OrderProductModel() {
    }

    public OrderProductModel(ProductShoppingCartModel product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductShoppingCartModel getProduct() {
        return product;
    }

    public void setProduct(ProductShoppingCartModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
