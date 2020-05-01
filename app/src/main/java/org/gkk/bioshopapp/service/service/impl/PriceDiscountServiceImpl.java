package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.gkk.bioshopapp.data.repository.PriceDiscountRepository;
import org.gkk.bioshopapp.service.service.PriceDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceDiscountServiceImpl implements PriceDiscountService {

    private final PriceDiscountRepository priceDiscountRepository;

    @Autowired
    public PriceDiscountServiceImpl(PriceDiscountRepository priceDiscountRepository) {
        this.priceDiscountRepository = priceDiscountRepository;
    }

    @Override
    public void removePromotion(String productId) {
        List<PriceDiscount> discounts = this.priceDiscountRepository.findAllPriceDiscountForCurrentPrice(productId);

        PriceDiscount priceDiscount = getLastPricePromotion(discounts);

        if (priceDiscount.getToDate().isAfter(LocalDateTime.now())){
            priceDiscount.setToDate(LocalDateTime.now());

            this.priceDiscountRepository.saveAndFlush(priceDiscount);
        }
    }

    private PriceDiscount getLastPricePromotion(List<PriceDiscount> priceDiscountList) {
        return priceDiscountList.stream()
                .min((pd1, pd2) -> pd2.getFromDate().compareTo(pd1.getFromDate()))
                .orElseThrow();
    }
}
