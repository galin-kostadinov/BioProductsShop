package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_discount")
public class PriceDiscount extends BaseEntity {

    private PriceHistory price;

    private Integer discount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public PriceDiscount() {
    }

    public PriceDiscount(PriceHistory price, Integer discount) {
        this.price = price;
        this.discount = discount;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    public PriceHistory getPrice() {
        return price;
    }

    public void setPrice(PriceHistory price) {
        this.price = price;
    }

    @Column
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Column(name ="from_date", updatable = false, nullable = false)
    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name ="to_date", nullable = false)
    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
