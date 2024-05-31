package com.binterpark.repository;

import com.binterpark.domain.ShoppingCart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository <ShoppingCart,Long> {

    List<ShoppingCart> findByUserId (Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShoppingCart s WHERE s.cartId = :cartId AND s.product.productId = :productId")
    void deleteItemFromCart(@Param("cartId") Long cartId, @Param("productId") Long productId);

}
