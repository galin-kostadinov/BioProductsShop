package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "shopping_cart_products")
public class ShoppingCartProduct implements Serializable {

    private ShoppingCart shoppingCart;

    private Product product;

    private Integer quantity;

    public ShoppingCartProduct() {
    }

    @Id
    @ManyToOne
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
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
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        BigDecimal price = this.product.getPrices()
                .stream()
                .filter(priceHistory -> priceHistory.getFromDate().isBefore(localDateTimeNow) &&
                        (priceHistory.getToDate() == null || priceHistory.getToDate().isAfter(localDateTimeNow)))
                .map(priceHistory -> {
                    BigDecimal pr = priceHistory.getPrice();

                    PriceDiscount priceDiscount = priceHistory.getPriceDiscountList()
                            .stream()
                            .filter(pd -> pd.getFromDate().isBefore(localDateTimeNow) &&
                                    (pd.getToDate() == null || pd.getToDate().isAfter(localDateTimeNow)))
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
