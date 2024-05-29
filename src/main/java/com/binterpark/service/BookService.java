package com.binterpark.service;

import com.binterpark.domain.Book;
import com.binterpark.exception.InvalidSearchKeywordException;
import com.binterpark.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> searchBooks (String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new InvalidSearchKeywordException("Search keyword must not be empty");
        }

        String processedKeyword = keyword.strip().toLowerCase();
        return bookRepository.findByTitleOrAuthor(processedKeyword);
    }

    public Book findBookById (Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
    }
}
