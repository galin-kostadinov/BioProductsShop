package org.gkk.bioshopapp.service.model.order;

import org.gkk.bioshopapp.service.model.product.ProductIdServiceModel;

public class OrderProductCreateServiceModel {

    private ProductIdServiceModel product;

    private Integer quantity;

    public OrderProductCreateServiceModel() {
    }

    public ProductIdServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductIdServiceModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
