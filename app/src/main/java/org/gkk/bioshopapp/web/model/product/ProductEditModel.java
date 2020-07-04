package org.gkk.bioshopapp.web.model.product;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductEditModel implements Serializable {

    private String id;

    private String name;

    private String description;

    private String imgUrl;

    private BigDecimal price;

    public ProductEditModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotBlank(message = "Product name is mandatory.")
    @Size(min = 3, message = "Product name size have to be min 3 characters.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
