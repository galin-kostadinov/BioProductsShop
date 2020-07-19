package org.gkk.bioshopapp.service.model.product;

import org.gkk.bioshopapp.data.model.ProductType;

import java.math.BigDecimal;

public class ProductCreateServiceModel {

    private String name;

    private String code;

    private String made;

    private ProductType type;

    private String description;

    private String imgUrl;

    private BigDecimal price;

    public ProductCreateServiceModel() {
    }

    public ProductCreateServiceModel(String name, String code, String made, ProductType type, String description, String imgUrl, BigDecimal price) {
        this.name = name;
        this.code = code;
        this.made = made;
        this.type = type;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
