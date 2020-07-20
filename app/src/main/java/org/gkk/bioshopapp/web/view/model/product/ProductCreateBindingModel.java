package org.gkk.bioshopapp.web.view.model.product;

import org.gkk.bioshopapp.data.model.ProductType;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductCreateBindingModel implements Serializable {
    private String name;

    private String code;

    private String made;

    private ProductType type;

    private String description;

    private String imgUrl;

    private BigDecimal price;

    public ProductCreateBindingModel() {
    }

    @NotBlank(message = "Product name is mandatory.")
    @Size(min = 3, message = "Product name size have to be min 3 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "Product code is mandatory.")
    @Size(min = 5, message = "Code size have to be min 5 characters.")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank(message = "Product made is mandatory.")
    @Size(min = 2, message = "Made size have to be min 2 characters.")
    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    @NotNull(message = "Product type is mandatory.")
    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    @NotBlank(message = "Product description is mandatory.")
    @Size(min = 10, message = "Description size have to be min 10 characters.")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank(message = "Product url is mandatory.")
    @URL(message = "Incorrect product url.")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NotNull(message = "Product price is mandatory.")
    @DecimalMin(value = "0.1", message = "Must be greater than or equal to 0.1.")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
