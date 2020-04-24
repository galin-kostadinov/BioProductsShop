package org.gkk.bioshopapp.service.model;

import java.math.BigDecimal;

public class ProductBuyServiceModel {
    private String id;

    private String name;

    private String imgUrl;

    private BigDecimal price;

    public ProductBuyServiceModel() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
