package org.gkk.bioshopapp.data.repository;

import org.gkk.bioshopapp.data.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o join o.buyer b where b.username =:username ORDER BY o.dateCreated desc ")
    List<Order> findAllByUsername(@Param("username") String username);
}
