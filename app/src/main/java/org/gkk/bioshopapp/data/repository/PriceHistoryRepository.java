package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, String> {

    @Query("select ph from PriceHistory ph join ph.product p where p.id =:productId and p.deleted = false and ph.toDate is null")
    Optional<PriceHistory> findOneByProductIdOrderByFromDateDesc(@Param("productId") String productId);
}
