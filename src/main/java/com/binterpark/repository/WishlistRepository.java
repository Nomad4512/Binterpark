package com.binterpark.repository;

import com.binterpark.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository <Wishlist,Long> {
}
