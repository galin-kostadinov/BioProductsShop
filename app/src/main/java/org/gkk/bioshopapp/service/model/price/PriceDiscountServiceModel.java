package org.gkk.bioshopapp.service.model.price;

import java.time.LocalDateTime;

public class PriceDiscountServiceModel {

    private Integer discount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public PriceDiscountServiceModel() {
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
