package com.binterpark.repository;

import com.binterpark.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository <Book, Long> {

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE lower(concat('%', :keyword, '%')) OR lower(b.author) LIKE lower(concat('%', :keyword, '%'))")
    List<Book> findByTitleOrAuthor(@Param("keyword") String keyword);

}