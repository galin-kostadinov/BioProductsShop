package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends BaseEntity {

    private LocalDateTime expiryDate;

    private Set<ShoppingCartProduct> shoppingCartProducts;

    private User buyer;

    public ShoppingCart() {
        this.shoppingCartProducts = new HashSet<>();
    }

    @Column(name = "expiry_sate", nullable = false)
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @OneToMany(mappedBy = "shoppingCart", orphanRemoval = true, cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    @Valid
    public Set<ShoppingCartProduct> getShoppingCartProducts() {
        return shoppingCartProducts;
    }

    public void setShoppingCartProducts(Set<ShoppingCartProduct> shoppingCartProducts) {
        this.shoppingCartProducts = shoppingCartProducts;
    }

    @OneToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
}
