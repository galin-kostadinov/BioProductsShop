package org.gkk.bioshopapp.service.model.product;

import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;

import java.math.BigDecimal;

public class ProductDiscountTableServiceModel implements PricePromotion {

    private String id;

    private String name;

    private String made;

    private String imgUrl;

    private PriceDiscountServiceModel priceDiscount;

    private BigDecimal price;

    private BigDecimal promotionalPrice;

    public ProductDiscountTableServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PriceDiscountServiceModel getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(PriceDiscountServiceModel priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getPromotionalPrice() {
        return promotionalPrice;
    }

    @Override
    public void setPromotionalPrice(BigDecimal promotionalPrice) {
        this.promotionalPrice = promotionalPrice;
    }
}
