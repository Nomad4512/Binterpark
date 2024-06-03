package com.binterpark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

    /*
    * 오버라이드한 생성자를 결정 못하는 이슈 발생 => 단일 생성자 + discountedPrice null?
    * 코드가 단순해지고 유지보수가 용이하나 유연이 감소하고 Null Pointer Exception 잠재적으로 발생가능, 명시적 의도 표현도 부족하다.
    * 양쪽의 장점을 모두 챙길 수 있는 Builder Pattern 사용 결정
    */
}
