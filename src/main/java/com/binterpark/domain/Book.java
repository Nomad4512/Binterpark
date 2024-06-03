package com.binterpark.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

}
