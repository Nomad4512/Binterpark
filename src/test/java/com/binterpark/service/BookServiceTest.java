package com.binterpark.service;

import com.binterpark.domain.Book;
import com.binterpark.dto.BookSpecificResponseDto;
import com.binterpark.exception.InvalidSearchKeywordException;
import com.binterpark.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private Book book1;
    private Book book2;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("결국 해내면 그만이다");
        book1.setAuthor("정영욱");

        book2 = new Book();
        book2.setBookId(2L);
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");

    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable!=null) {
            closeable.close();
        }
    }

    @Test
    void searchBooks_shouldReturnBooks_whenKeywordIsProvided() {
        String keyword = "결국";
        when(bookRepository.findByTitleOrAuthor(keyword)).thenReturn(Arrays.asList(book1));

        List<BookSpecificResponseDto> result = bookService.searchBooks(keyword);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("결국 해내면 그만이다", result.get(0).getTitle());
    }

    @Test
    void searchBooks_shouldThrowException_whenKeywordIsEmpty() {
        Exception exception = assertThrows(InvalidSearchKeywordException.class, () -> {
            bookService.searchBooks("");
        });

        String expectedMessage = "Search keyword must not be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findBookById_shouldReturnBook_whenBookExists() {
        Long bookId = 2L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book2));

        BookSpecificResponseDto result = bookService.findBookById(bookId);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void findBookById_shouldThrowException_whenBookDoesNotExist() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findBookById(bookId);
        });

        String expectedMessage = "Book not found with id " + bookId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}