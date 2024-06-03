package com.binterpark.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

}
