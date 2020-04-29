package org.gkk.bioshopapp.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

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
        LocalDateTime dateCreated = order.getDateCreated();

        BigDecimal price = this.product.getPrices()
                .stream()
                .filter(priceHistory -> priceHistory.getFromDate().isBefore(dateCreated) &&
                        (priceHistory.getToDate() == null || priceHistory.getToDate().isAfter(dateCreated)))
                .map(priceHistory -> {
                    BigDecimal pr = priceHistory.getPrice();

                    PriceDiscount priceDiscount = priceHistory.getPriceDiscountList()
                            .stream()
                            .filter(pd -> pd.getFromDate().isBefore(dateCreated) &&
                                    (pd.getToDate() == null || pd.getToDate().isAfter(dateCreated)))
                            .findFirst()
                            .orElse(null);

                    if (priceDiscount != null) {
                        pr = (pr.multiply(BigDecimal.valueOf(1 - priceDiscount.getDiscount() / 100.0)))
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    return pr;
                })
                .findFirst()
                .orElse(null);

        return price == null ? BigDecimal.ZERO : price.multiply(BigDecimal.valueOf(getQuantity()));
    }
}
