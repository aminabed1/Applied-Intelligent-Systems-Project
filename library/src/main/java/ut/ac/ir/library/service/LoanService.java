package ut.ac.ir.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ut.ac.ir.library.dto.request.LoanRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.entity.Loan;
import ut.ac.ir.library.exception.custom.ConflictException;
import ut.ac.ir.library.exception.custom.NotFoundException;
import ut.ac.ir.library.repository.BookRepository;
import ut.ac.ir.library.repository.LoanRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ApiResponse<Void> insert(LoanRequest request) {
        Book book = findBookById(request.getBookId());

        if (!book.isAvailable()) {
            throw new ConflictException("Book is not available.");
        }

        if (book.isDeleted()) {
            throw new ConflictException("Book has been deleted from the library.");

        }

        String borrowerName = request.getBorrowerName();
        book.setAvailable(false);
        Loan loan = Loan.builder()
                .book(book)
                .borrowerName(borrowerName)
                .loanDate(request.getLoanDate())
                .returnDate(request.getReturnDate())
                .build();
        loanRepository.save(loan);
        return ApiResponse.success("Loan inserted successfully.", HttpStatus.CREATED);
    }

    @Transactional
    public ApiResponse<Void> returnBook(Long id) {
        Book book = findBookById(id);

        if (book.isAvailable()) {
            throw new ConflictException("Book has not been borrowed.");
        }

        Optional<Loan> optionalLoan = loanRepository.findByBookIdAndActualReturnDateIsNull(id);
        if (optionalLoan.isEmpty()) {
            throw new NotFoundException("Loan not found.");
        }
        Loan loan = optionalLoan.get();

        book.setAvailable(true);
        loan.setActualReturnDate(LocalDate.now());
        return ApiResponse.success("book returned to library.", HttpStatus.OK);
    }

    private Book findBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new NotFoundException(String.format("Book with id \"%d\" not found.", id));
        }

        return optionalBook.get();
    }
}
