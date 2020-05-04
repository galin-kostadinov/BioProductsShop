package org.gkk.bioshopapp.data.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;

    private String made;

    private Category category;

    private String description;

    private String imgUrl;

    private List<PriceHistory> prices;

    private boolean isDeleted;

    public Product() {
        this.prices = new ArrayList<>();
    }

    @Column(name = "name", length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "made", length = 50, nullable = false)
    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(name = "description", length = 65535, columnDefinition = "text", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "img_url", nullable = false)
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.PERSIST})
    public List<PriceHistory> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceHistory> prices) {
        this.prices = prices;
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Transient
    public PriceHistory getLastAssignedAmountFromHistory() {
        return this.prices.stream()
                .min((p1, p2) -> p2.getFromDate().compareTo(p1.getFromDate()))
                .orElseThrow();
    }
}
