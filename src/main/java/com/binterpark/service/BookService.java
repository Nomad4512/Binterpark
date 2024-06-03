package com.binterpark.service;

import com.binterpark.domain.Book;
import com.binterpark.dto.BookSpecificResponseDto;
import com.binterpark.exception.InvalidSearchKeywordException;
import com.binterpark.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 도서 검색 기능
    public List<BookSpecificResponseDto> searchBooks (String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new InvalidSearchKeywordException("Search keyword must not be empty");
        }

        String processedKeyword = keyword.strip().toLowerCase();
        List<Book> books = bookRepository.findByTitleOrAuthor(processedKeyword);

        return books.stream()
                .map(this::convertBookToDto)
                .collect(Collectors.toList());
    }

    // 도서 상세 정보 조회
    public BookSpecificResponseDto findBookById (Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));

        return convertBookToDto(book);
    }

    private BookSpecificResponseDto convertBookToDto (Book book) {
        BookSpecificResponseDto dto = new BookSpecificResponseDto();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPrice(book.getPrice());
        dto.setDiscountedPrice(book.getDiscountedPrice());
        dto.setSoldCount(book.getSoldCount());
        dto.setPublisher(book.getPublisher());
        dto.setBookDescription(book.getBookDescription());

        return dto;
    }
}
