package org.gkk.bioshopapp.service.model.order;

import org.gkk.bioshopapp.service.model.product.ProductOrderDetailsServiceModel;

import java.math.BigDecimal;

public class OrderProductServiceModel {

    private ProductOrderDetailsServiceModel product;

    private Integer quantity;

    private BigDecimal totalPrice;

    public OrderProductServiceModel() {
    }

    public ProductOrderDetailsServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductOrderDetailsServiceModel product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
