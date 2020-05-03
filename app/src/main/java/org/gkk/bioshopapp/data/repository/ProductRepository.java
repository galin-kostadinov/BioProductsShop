package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Modifying
    @Transactional
    @Query("update Product p set p.deleted = true where p.id =:id and p.deleted = false ")
    void setProductDeletedTrue(@Param("id") String id);

    @Query("select p from Product p join p.prices ps join ps.priceDiscountList pd  " +
            "where p.deleted = false and (ps.toDate is null or ps.toDate > :timeNow) and :timeNow between pd.fromDate and pd.toDate group by p.id")
    List<Product> findAllInPromotion(@Param("timeNow") LocalDateTime timeNow);

    List<Product> findAllByDeletedIsFalse();

    Optional<Product> findByIdAndDeletedIsFalse(String id);
}
