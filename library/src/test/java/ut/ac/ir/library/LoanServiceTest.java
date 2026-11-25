package ut.ac.ir.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ut.ac.ir.library.dto.request.LoanRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.entity.Loan;
import ut.ac.ir.library.exception.custom.ConflictException;
import ut.ac.ir.library.exception.custom.NotFoundException;
import ut.ac.ir.library.repository.BookRepository;
import ut.ac.ir.library.repository.LoanRepository;
import ut.ac.ir.library.service.LoanService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanService loanService;

    private Book availableBook;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        availableBook = Book.builder()
                .id(1L)
                .title("Savushun")
                .authorName("Simin Daneshvar")
                .isAvailable(true)
                .isDeleted(false)
                .build();

        loanRequest = new LoanRequest();
        loanRequest.setBookId(1L);
        loanRequest.setBorrowerName("Sara Mohammadi");
        loanRequest.setLoanDate(LocalDate.now());
        loanRequest.setReturnDate(LocalDate.now().plusDays(14));
    }

    @Test
    void insert_WhenBookIsAvailableAndNotDeleted_ShouldCreateLoan() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));

        ApiResponse<Void> response = loanService.insert(loanRequest);

        assertFalse(availableBook.isAvailable());
        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository, times(1)).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();
        assertEquals("Sara Mohammadi", savedLoan.getBorrowerName());
        assertEquals(availableBook, savedLoan.getBook());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void insert_WhenBookNotFound_ShouldThrowNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.insert(loanRequest));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void insert_WhenBookIsNotAvailable_ShouldThrowConflictException() {
        availableBook.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));

        assertThrows(ConflictException.class, () -> loanService.insert(loanRequest));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void insert_WhenBookIsDeleted_ShouldThrowException() {
        availableBook.setDeleted(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));

        assertThrows(ConflictException.class, () -> loanService.insert(loanRequest));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void returnBook_WhenBookIsBorrowed_ShouldReturnBookSuccessfully() {
        availableBook.setAvailable(false);
        Loan activeLoan = Loan.builder()
                .Id(10L)
                .book(availableBook)
                .borrowerName("Sara Mohammadi")
                .loanDate(LocalDate.now().minusDays(5))
                .returnDate(LocalDate.now().plusDays(9))
                .actualReturnDate(null)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));
        when(loanRepository.findByBookIdAndActualReturnDateIsNull(1L)).thenReturn(Optional.of(activeLoan));

        ApiResponse<Void> response = loanService.returnBook(1L);

        assertTrue(availableBook.isAvailable());
        assertEquals(LocalDate.now(), activeLoan.getActualReturnDate());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void returnBook_WhenBookIsAlreadyAvailable_ShouldThrowConflictException() {
        availableBook.setAvailable(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));

        assertThrows(ConflictException.class, () -> loanService.returnBook(1L));
        verify(loanRepository, never()).findByBookIdAndActualReturnDateIsNull(any());
    }

    @Test
    void returnBook_WhenActiveLoanNotFound_ShouldThrowNotFoundException() {
        availableBook.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));
        when(loanRepository.findByBookIdAndActualReturnDateIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loanService.returnBook(1L));
    }
}