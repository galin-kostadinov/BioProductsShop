package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.gkk.bioshopapp.data.model.PriceHistory;
import org.gkk.bioshopapp.data.repository.PriceHistoryRepository;
import org.gkk.bioshopapp.error.PriceHishoryNotFoundException;
import org.gkk.bioshopapp.service.model.price.PriceDiscountServiceModel;
import org.gkk.bioshopapp.service.service.PriceHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PriceHistoryServiceImplTest extends TestBase {

    @MockBean
    PriceHistoryRepository priceHistoryRepository;

    @Autowired
    PriceHistoryService priceHistoryService;

    @Test
    public void setDiscount_whenProductNotExist_ShouldThrowException() {
        String productId = UUID.randomUUID().toString();
        PriceDiscountServiceModel newPriceDiscount = new PriceDiscountServiceModel();
        newPriceDiscount.setDiscount(10);
        newPriceDiscount.setFromDate(LocalDateTime.now().plusMinutes(10));
        newPriceDiscount.setToDate(LocalDateTime.now().plusDays(10));

        assertThrows(PriceHishoryNotFoundException.class, () -> priceHistoryService.setDiscount(productId, newPriceDiscount));
    }

    @Test
    public void setDiscount_whenToDateIsNotValid_ShouldThrowException() {
        String productId = UUID.randomUUID().toString();
        PriceDiscountServiceModel newPriceDiscount = new PriceDiscountServiceModel();
        newPriceDiscount.setDiscount(10);
        newPriceDiscount.setFromDate(LocalDateTime.now().plusMinutes(10));
        newPriceDiscount.setToDate(LocalDateTime.now().minusHours(10));

        assertThrows(IllegalArgumentException.class, () -> priceHistoryService.setDiscount(productId, newPriceDiscount));
    }

    @Test
    public void setDiscount_whenPriceDiscountNotExistInPriceHistory_ShouldAddNewPriceDiscount() {
        String productId = UUID.randomUUID().toString();
        PriceDiscountServiceModel newPriceDiscount = new PriceDiscountServiceModel();
        newPriceDiscount.setDiscount(10);
        newPriceDiscount.setFromDate(LocalDateTime.now().plusMinutes(10));
        newPriceDiscount.setToDate(LocalDateTime.now().plusDays(10));

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPriceDiscountList(new ArrayList<>());

        Mockito.when(priceHistoryRepository.findOneByProductIdOrderByFromDateDesc(productId)).thenReturn(Optional.of(priceHistory));

        priceHistoryService.setDiscount(productId, newPriceDiscount);
        ArgumentCaptor<PriceHistory> argumentPriceHistory = ArgumentCaptor.forClass(PriceHistory.class);
        Mockito.verify(priceHistoryRepository).saveAndFlush(argumentPriceHistory.capture());

        PriceHistory priceHistoryResult = argumentPriceHistory.getValue();

        assertNotNull(priceHistoryResult);
        List<PriceDiscount> priceDiscountsListResult = priceHistory.getPriceDiscountList();
        assertNotNull(priceDiscountsListResult);

        assertEquals(1, priceDiscountsListResult.size());

        PriceDiscount priceDiscountResult = priceDiscountsListResult.get(0);

        assertEquals(newPriceDiscount.getFromDate(), priceDiscountResult.getFromDate());
        assertEquals(newPriceDiscount.getToDate(), priceDiscountResult.getToDate());
        assertEquals(newPriceDiscount.getDiscount(), priceDiscountResult.getDiscount());
    }
}