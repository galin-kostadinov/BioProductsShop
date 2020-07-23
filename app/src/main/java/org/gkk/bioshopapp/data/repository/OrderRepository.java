package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o join o.buyer b where b.username =:username ORDER BY o.dateCreated desc ")
    Page<Order> findAllByUsername(@Param("username") String username, Pageable pageable);
}
