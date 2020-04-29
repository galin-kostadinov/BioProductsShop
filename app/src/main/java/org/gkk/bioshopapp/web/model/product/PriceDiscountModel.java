package org.gkk.bioshopapp.web.model.product;

public class PriceDiscountModel {

    private Integer discount;

    private String fromDate;

    private String toDate;

    public PriceDiscountModel() {
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
