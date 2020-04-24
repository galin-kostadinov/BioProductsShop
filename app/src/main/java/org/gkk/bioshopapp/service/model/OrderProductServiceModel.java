package org.gkk.bioshopapp.service.model;

public class OrderProductServiceModel {

    private ProductServiceModel product;

    private Integer quantity;

    public OrderProductServiceModel() {
    }

    public ProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductServiceModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
