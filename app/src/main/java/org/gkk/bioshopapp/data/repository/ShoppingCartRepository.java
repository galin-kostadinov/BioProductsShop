package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {

    @Query("SELECT s FROM ShoppingCart s join s.buyer b where b.username =:username")
    Optional<ShoppingCart> findByBuyer(@Param("username") String username);

    void deleteAllByExpiryDateIsLessThanEqual(LocalDateTime now);

    @Query("SELECT s FROM ShoppingCart s join s.shoppingCartProducts pl where pl.product.id =:productId")
    List<ShoppingCart> findAllByProductId(@Param("productId") String productId);
}
