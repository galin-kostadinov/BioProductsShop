package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prices_history")
public class PriceHistory extends BaseEntity {

    private BigDecimal price;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private Product product;

    private List<PriceDiscount> priceDiscountList;

    public PriceHistory() {
        this.priceDiscountList = new ArrayList<>();
    }

    public PriceHistory(BigDecimal price, LocalDateTime fromDate) {
        this.priceDiscountList = new ArrayList<>();
        this.price = price;
        this.fromDate = fromDate;
    }

    @Column(nullable = false, updatable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "from_date", nullable = false, updatable = false)
    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name = "to_date")
    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @OneToMany(mappedBy = "price", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.PERSIST})
    public List<PriceDiscount> getPriceDiscountList() {
        return priceDiscountList;
    }

    public void setPriceDiscountList(List<PriceDiscount> priceDiscountList) {
        this.priceDiscountList = priceDiscountList;
    }

    @Transient
    public PriceDiscount getLastPromotion() {
        return this.priceDiscountList.stream()
                .min((pd1, pd2) -> pd2.getFromDate().compareTo(pd1.getFromDate()))
                .orElseThrow();
    }
}
