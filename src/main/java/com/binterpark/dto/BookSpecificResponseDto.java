package com.binterpark.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookSpecificResponseDto {

    private Long bookId;                // 책 ID
    private String category;            // 카테고리
    private String name;                // 제목
    private String author;              // 저자
    private String isbn;                // ISBN
    private int price;                  // 가격
    private Integer discountedPrice;    // 할인 가격 (null일 수 있음)
    private int soldCount;              // 판매량
    private String publisher;           // 출판사
    private LocalDateTime publishedDate;// 발행일
    private String bookDescription;     // 책 설명
    private String mainImage;           // 표지 이미지 URL
    private String specificImage;        // 상세 이미지 URL

    // 할인 가격이 있는 경우의 생성자
    public BookSpecificResponseDto(Long bookId, String category, String name, String author, String isbn, int price, Integer discountedPrice,
                                   int soldCount, String publisher, LocalDateTime publishedDate, String bookDescription, String mainImage, String specificImage) {
        this.bookId = bookId;
        this.category = category;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.soldCount = soldCount;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.bookDescription = bookDescription;
        this.mainImage = mainImage;
        this.specificImage = specificImage;
    }

    // 할인 가격이 없는 경우의 생성자
    public BookSpecificResponseDto(Long bookId, String category, String name, String author, String isbn, int price,
                                   int soldCount, String publisher, LocalDateTime publishedDate, String bookDescription, String mainImage, String specificImage) {
        this.bookId = bookId;
        this.category = category;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.discountedPrice = null; // 할인 가격을 null로 설정
        this.soldCount = soldCount;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.bookDescription = bookDescription;
        this.mainImage = mainImage;
        this.specificImage = specificImage;
    }
}
