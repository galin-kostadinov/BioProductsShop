package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByPromotionNotNull();
}
