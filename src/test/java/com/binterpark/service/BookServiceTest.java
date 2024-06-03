package com.binterpark.service;

import com.binterpark.domain.Book;
import com.binterpark.dto.BookSpecificResponseDto;
import com.binterpark.exception.InvalidSearchKeywordException;
import com.binterpark.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    Book book1 = Book.builder()
            .bookId(1L)
            .name("결국 해내면 그만이다")
            .author("정영욱")
            .build();


    Book book2 = Book.builder()
            .bookId(2L)
            .name("Clean Code")
            .author("Robert C. Martin")
            .build();

    @Test
    void searchBooks_shouldReturnBooks_whenKeywordIsProvided() {
        // Given
        String keyword = "결국";
        when(bookRepository.findByTitleOrAuthor(keyword)).thenReturn(Arrays.asList(book1));

        // When
        List<BookSpecificResponseDto> result = bookService.searchBooks(keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("결국 해내면 그만이다", result.get(0).getName());
    }

    @Test
    void searchBooks_shouldThrowException_whenKeywordIsEmpty() {
        // When
        Exception exception = assertThrows(InvalidSearchKeywordException.class, () -> {
            bookService.searchBooks("");
        });

        // Then
        String expectedMessage = "검색어를 입력해주세요.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findBookById_shouldReturnBook_whenBookExists() {
        // Given
        Long bookId = 2L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book2));

        // When
        BookSpecificResponseDto result = bookService.findBookById(bookId);

        // Then
        assertNotNull(result);
        assertEquals("Clean Code", result.getName());
    }

    @Test
    void findBookById_shouldThrowException_whenBookDoesNotExist() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findBookById(bookId);
        });

        // Then
        String expectedMessage = "책 정보를 찾을 수 없습니다. ID : " + bookId;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}