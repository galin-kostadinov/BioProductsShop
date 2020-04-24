package org.gkk.bioshopapp.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "order_products")
public class OrderProduct implements Serializable {

    @JsonIgnore
    private Order order;

    @JsonIgnore
    private Product product;

    private Integer quantity;

    public OrderProduct() {
    }

    @Id
    @ManyToOne
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Id
    @ManyToOne
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Transient
    public BigDecimal getTotalPrice() {
        return getProduct().getPrice().multiply(BigDecimal.valueOf(getQuantity()));
    }
}
