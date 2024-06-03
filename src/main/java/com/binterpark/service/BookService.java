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
            throw new InvalidSearchKeywordException("검색어를 입력해주세요.");
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
                .orElseThrow(() -> new EntityNotFoundException("책 정보를 찾을 수 없습니다. ID : " + id));

        return convertBookToDto(book);
    }

    private BookSpecificResponseDto convertBookToDto (Book book) {
        return BookSpecificResponseDto.builder()
                .bookId(book.getBookId())
                .category(book.getCategory())
                .name(book.getName())  // 변경된 필드명 사용
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .discountedPrice(book.getDiscountedPrice())
                .soldCount(book.getSoldCount())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .bookDescription(book.getDescription())
                .mainImage(book.getMainImage())
                .specificImage(book.getSpecificImage())
                .build();
    }
}
