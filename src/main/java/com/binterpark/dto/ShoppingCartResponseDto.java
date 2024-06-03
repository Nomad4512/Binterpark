package com.binterpark.dto;

import com.binterpark.domain.Product;
import com.binterpark.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ShoppingCartResponseDto {

    private Long cartId;
    private Long userId;
    private Product product;
    private int quantity;
    private LocalDateTime dateAdded;
}
