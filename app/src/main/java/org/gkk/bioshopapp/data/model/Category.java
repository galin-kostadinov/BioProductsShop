package org.gkk.bioshopapp.data.model;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private ProductType name;

    public Category() {
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, updatable = false)
    public ProductType getName() {
        return name;
    }

    public void setName(ProductType name) {
        this.name = name;
    }
}
