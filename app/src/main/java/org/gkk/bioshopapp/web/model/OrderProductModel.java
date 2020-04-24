package org.gkk.bioshopapp.web.model;

import java.io.Serializable;

public class OrderProductModel implements Serializable {

    private ProductBuyModel product;

    private Integer quantity;

    public OrderProductModel() {
    }

    public OrderProductModel(ProductBuyModel product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductBuyModel getProduct() {
        return product;
    }

    public void setProduct(ProductBuyModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
