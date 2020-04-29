package org.gkk.bioshopapp.service.service;

import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;

public interface PriceHistoryService {

    void setDiscount(String id, PriceDiscountServiceModel priceDiscountServiceModel);
}
