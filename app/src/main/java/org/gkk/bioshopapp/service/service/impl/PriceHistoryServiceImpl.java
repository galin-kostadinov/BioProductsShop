package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.constant.ErrorMessageConstant;
import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.gkk.bioshopapp.data.model.PriceHistory;
import org.gkk.bioshopapp.data.repository.PriceHistoryRepository;
import org.gkk.bioshopapp.error.PriceHishoryNotFoundException;
import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;
import org.gkk.bioshopapp.service.service.PriceHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.gkk.bioshopapp.constant.ErrorMessageConstant.*;

@Service
public class PriceHistoryServiceImpl implements PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PriceHistoryServiceImpl(PriceHistoryRepository priceHistoryRepository, ModelMapper modelMapper) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void setDiscount(String productId, PriceDiscountServiceModel model) {
        if (model.getToDate().isBefore(LocalDateTime.now())) {
           return;
        }

        PriceHistory priceHistory = this.priceHistoryRepository.findOneByProductIdOrderByFromDateDesc(productId)
                .orElseThrow(()->new PriceHishoryNotFoundException(PRICE_HISTORY_NOT_FOUND));
        List<PriceDiscount> priceDiscounts = priceHistory.getPriceDiscountList();

        if (!priceDiscounts.isEmpty()) {
            PriceDiscount lastPriceDiscount = getLastPricePromotion(priceDiscounts);

            if (lastPriceDiscount.getToDate().isAfter(model.getFromDate())) {
                lastPriceDiscount.setToDate(model.getFromDate().minusSeconds(1));
            }
        }

        PriceDiscount newPriceDiscount = this.modelMapper.map(model, PriceDiscount.class);
        newPriceDiscount.setFromDate(LocalDateTime.now());


        newPriceDiscount.setPrice(priceHistory);
        priceDiscounts.add(newPriceDiscount);

        this.priceHistoryRepository.saveAndFlush(priceHistory);
    }

    private PriceDiscount getLastPricePromotion(List<PriceDiscount> priceDiscountList) {
        return priceDiscountList.stream()
                .min((pd1, pd2) -> pd2.getFromDate().compareTo(pd1.getFromDate()))
                .orElseThrow();
    }
}
