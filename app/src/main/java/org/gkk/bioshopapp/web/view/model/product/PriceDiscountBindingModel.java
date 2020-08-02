package org.gkk.bioshopapp.web.view.model.product;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PriceDiscountBindingModel {

    private Integer discount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public PriceDiscountBindingModel() {
    }

    @NotNull(message = "Discount is mandatory.")
    @Min(value = 1, message = "Min discount value is 1%.")
    @Max(value = 90, message = "Max discount value is 90%.")
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @NotNull(message = "The start date is mandatory.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    @NotNull(message = "The end date is mandatory.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "The date cannot be in the past.")
    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
