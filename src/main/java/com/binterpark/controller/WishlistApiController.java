package com.binterpark.controller;

import com.binterpark.domain.Wishlist;
import com.binterpark.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class WishlistApiController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<String> addToWish(@RequestParam(value = "userId") Long userId, @RequestParam(value = "productId") Long productId) {
        Wishlist wish = wishlistService.addToWish(userId, productId);
        return ResponseEntity.ok().body("상품이 찜에 추가되었습니다.");
    }
}
