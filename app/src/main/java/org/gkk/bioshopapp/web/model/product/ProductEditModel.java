package org.gkk.bioshopapp.web.model.product;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductEditModel implements Serializable {
    private String name;

    private String description;

    private String imgUrl;

    private BigDecimal price;

    public ProductEditModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
