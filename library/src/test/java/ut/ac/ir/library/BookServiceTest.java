package ut.ac.ir.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import ut.ac.ir.library.dto.request.EditBookInfoRequest;
import ut.ac.ir.library.dto.request.InsertBookRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.exception.custom.NotFoundException;
import ut.ac.ir.library.repository.BookRepository;
import ut.ac.ir.library.service.BookService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = Book.builder()
                .id(1L)
                .title("Boof-e Koor")
                .authorName("Sadegh Hedayat")
                .isbn("978-9644480850")
                .publishYear(1315)
                .isAvailable(true)
                .isDeleted(false)
                .build();
    }

    @Test
    void insertBook_ShouldSaveBookAndReturnCreated() {
        // Arrange
        InsertBookRequest request = new InsertBookRequest();
        request.setTitle("Boof-e Koor");
        request.setAuthorName("Sadegh Hedayat");
        request.setIsbn("978-9644480850");
        request.setPublishYear(1315);

        ApiResponse<Void> response = bookService.insertBook(request);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();
        assertEquals("Boof-e Koor", savedBook.getTitle());
        assertEquals("Sadegh Hedayat", savedBook.getAuthorName());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void getAllWithFilters_ShouldReturnPagedBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);

        ApiResponse<Page<Book>> response = bookService.getAllWithFilters("Boof", "Hedayat", 1315, true, pageable);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getTotalElements());
        assertEquals("Boof-e Koor", response.getData().getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        ApiResponse<Book> response = bookService.findById(1L);

        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
        assertEquals("Boof-e Koor", response.getData().getTitle());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void findById_WhenBookDoesNotExist_ShouldThrowNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findById(99L));
    }

    @Test
    void editInfo_WhenBookExists_ShouldUpdateFields() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        EditBookInfoRequest request = new EditBookInfoRequest();
        request.setTitle("Kelidar");
        request.setAuthorName("Mahmoud Dowlatabadi");
        request.setPublishYear(1357);

        ApiResponse<Void> response = bookService.editInfo(1L, request);

        // Assert
        assertEquals("Kelidar", sampleBook.getTitle());
        assertEquals("Mahmoud Dowlatabadi", sampleBook.getAuthorName());
        assertEquals(1357, sampleBook.getPublishYear());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void delete_WhenBookExists_ShouldSoftDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        ApiResponse<Void> response = bookService.delete(1L);

        assertFalse(sampleBook.isAvailable());
        assertTrue(sampleBook.isDeleted());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}