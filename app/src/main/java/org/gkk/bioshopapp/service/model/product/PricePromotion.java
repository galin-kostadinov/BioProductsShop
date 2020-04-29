package org.gkk.bioshopapp.service.model.product;

import java.math.BigDecimal;

public interface PricePromotion {

    BigDecimal getPrice();

    void setPrice(BigDecimal price);

    BigDecimal getPromotionalPrice();

    void setPromotionalPrice(BigDecimal promotionalPrice);
}
