package org.gkk.bioshopapp.web.model.product;

import org.gkk.bioshopapp.data.model.ProductType;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductCreateModel implements Serializable {
    private String name;

    private String made;

    private ProductType type;

    private String description;

    private String imgUrl;

    private BigDecimal price;

    public ProductCreateModel() {
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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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
