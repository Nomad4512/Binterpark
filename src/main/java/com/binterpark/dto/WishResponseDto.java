package com.binterpark.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishResponseDto {

    private Long wishId;
    private Long userId;
    private Long productId;
    private LocalDateTime dateAdded;

}
