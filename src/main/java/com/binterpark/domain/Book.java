package com.binterpark.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "boot_title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "book_price")
    private int price;

    @Column(name = "discounted_book_price")
    private int discountedPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "sold_count", nullable = false)
    private int soldCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "book_description")
    private String bookDescription;
}
