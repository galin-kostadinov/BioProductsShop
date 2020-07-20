package org.gkk.bioshopapp.web.api.model.product;

import java.time.LocalDateTime;

public class PriceDiscountResponseModel {

    private Integer discount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public PriceDiscountResponseModel() {
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
