package com.binterpark.repository;

import com.binterpark.domain.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository <Wishlist,Long> {

    List<Wishlist> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist s WHERE s.wishId = :wishId AND s.product.productId = :productId")
    void deleteItemFromWish(@Param("wishId") Long wishId, @Param("productId") Long productId);
}
