package org.gkk.bioshopapp.data.repository;


import org.gkk.bioshopapp.data.model.PriceDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceDiscountRepository extends JpaRepository<PriceDiscount, String> {

    @Query("select pd from PriceDiscount pd where pd.price.product.id =:productId " +
            "and pd.price.product.deleted = false and pd.price.toDate is null")
    List<PriceDiscount> findAllPriceDiscountForCurrentPrice(@Param("productId")String productId);
}
