package com.binterpark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookSearchResponseDto {

    // 제시정보 : 책표지, 카테고리, 이름, 저자, 출판사, 발행일, 가격, 할인가격(있으면), 판매량
    // 정렬기준 :  정확도, 판매량, 신상품, 상품명, 가격높은낮은순, 리뷰 많음적음순, 평점 높음낮음순

    private Long bookId;                    // 책 ID
    private String mainImage;               // 표지 이미지 URL
    private String category;                // 카테고리
    private String name;                    // 이름
    private String author;                  // 저자
    private String publisher;               // 출판사
    private LocalDateTime publishedDate;    // 발행일
    private int price;                      // 가격
    private Integer discountedPrice;        // 할인 가격 (null일 수 있음)
    private int soldCount;                  // 판매량

    private int viewCount;                  // 조회수
    private int reviewCount;                // 리뷰수

    // 할인 가격이 있는 경우의 생성자
    public BookSearchResponseDto(String category, String name, String author, String publisher,
                                 LocalDateTime publishedDate, int price, int discountedPrice,
                                 int soldCount, int viewCount, int reviewCount) {
        this.category = category;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.soldCount = soldCount;
        this.viewCount = viewCount;
        this.reviewCount = reviewCount;
    }

    // 할인 가격이 없는 경우의 생성자
    public BookSearchResponseDto(String category, String name, String author, String publisher,
                                 LocalDateTime publishedDate, int price, int soldCount,
                                 int viewCount, int reviewCount) {
        this.category = category;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.price = price;
        this.discountedPrice = null; // 할인 가격을 null로 설정
        this.soldCount = soldCount;
        this.viewCount = viewCount;
        this.reviewCount = reviewCount;
    }

}
