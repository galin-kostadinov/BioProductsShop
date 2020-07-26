package org.gkk.bioshopapp.service;

import org.gkk.bioshopapp.base.TestBase;
import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.gkk.bioshopapp.data.repository.PriceDiscountRepository;
import org.gkk.bioshopapp.service.model.log.LogServiceModel;
import org.gkk.bioshopapp.service.service.PriceDiscountService;
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

class PriceDiscountServiceImplTest extends TestBase {

    @MockBean
    PriceDiscountRepository priceDiscountRepository;

    @Autowired
    PriceDiscountService priceDiscountService;

    @Test
    public void removePromotion() {
        String productId = UUID.randomUUID().toString();

        List<PriceDiscount> discounts = new ArrayList<>();

        PriceDiscount pd = new PriceDiscount();
        pd.setDiscount(22);
        pd.setId(UUID.randomUUID().toString());
        pd.setFromDate(LocalDateTime.now().minusDays(5));
        pd.setToDate(LocalDateTime.now().plusDays(5));

        discounts.add(pd);

        Mockito.when(priceDiscountRepository.findAllPriceDiscountForCurrentPrice(productId)).thenReturn(discounts);

        priceDiscountService.removePromotion(productId);

        ArgumentCaptor<PriceDiscount> argumentPriceDiscount = ArgumentCaptor.forClass(PriceDiscount.class);
        Mockito.verify(priceDiscountRepository).saveAndFlush(argumentPriceDiscount.capture());

        PriceDiscount priceDiscountDB = argumentPriceDiscount.getValue();

        assertNotNull(priceDiscountDB);
        assertEquals(pd.getId(), priceDiscountDB.getId());
        assertEquals(pd.getPrice(), priceDiscountDB.getPrice());
        assertEquals(pd.getDiscount(), priceDiscountDB.getDiscount());
        assertTrue(priceDiscountDB.getToDate().isBefore(LocalDateTime.now().plusMinutes(1)));
    }
}