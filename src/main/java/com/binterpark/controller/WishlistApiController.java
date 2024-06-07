package com.binterpark.controller;

import com.binterpark.dto.WishResponseDto;
import com.binterpark.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class WishlistApiController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWish(@RequestParam(value = "userId") Long userId, @RequestParam(value = "productId") Long productId) {

        try {
            WishResponseDto wishlist = wishlistService.addToWish(userId, productId);
            return ResponseEntity.ok().body(wishlist);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getWishlist (@RequestParam(value = "userId") Long userId) {
        try {
            List<WishResponseDto> wishlist = wishlistService.getWishList(userId);
            return ResponseEntity.ok(wishlist);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e ) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromWish(@RequestParam(value = "wishId") Long wishId,
                                                @RequestParam(value = "productId") Long productId) {
        try {
            boolean removed = wishlistService.removeItemFromWish(wishId,productId);
            if (removed) {
                return ResponseEntity.ok("상품이 찜에서 삭제되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("찜에서 상품을 삭제하는데 실패했습니다.");
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
