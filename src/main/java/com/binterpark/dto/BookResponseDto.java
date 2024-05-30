package com.binterpark.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BookResponseDto {

    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private int price;
    private int discountedPrice;
    private int soldCount;
    private String publisher;
    private String bookDescription;
}
