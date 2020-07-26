package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private LocalDateTime dateCreated;

    private OrderStatus status;

    private List<OrderProduct> orderProducts;

    private User buyer;

    public Order() {
        this.orderProducts = new ArrayList<>();
    }

    @Column(name = "date_created", nullable = false, updatable = false)
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "order", orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Valid
    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @Transient
    public BigDecimal getTotalOrderPrice() {
        return getOrderProducts()
                .stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public int getNumberOfProducts() {
        return this.orderProducts.size();
    }
}
