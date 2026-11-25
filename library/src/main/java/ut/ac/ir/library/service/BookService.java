package ut.ac.ir.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ut.ac.ir.library.dto.request.EditBookInfoRequest;
import ut.ac.ir.library.dto.request.InsertBookRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.exception.custom.NotFoundException;
import ut.ac.ir.library.repository.BookRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public ApiResponse<Void> insertBook(InsertBookRequest request) {
        Book newBook = Book.builder()
                .title(request.getTitle())
                .authorName(request.getAuthorName())
                .isbn(request.getIsbn())
                .publishYear(request.getPublishYear())
                .build();
        bookRepository.save(newBook);

        return ApiResponse.success("Book inserted successfully.", HttpStatus.CREATED);
    }

    public ApiResponse<Page<Book>> getAllWithFilters(String title, String authorName, Integer publishYear, Boolean isAvailable, Pageable pageable) {
        Specification<Book> spec = BookSpecification.filterBooks(title, authorName, publishYear, isAvailable);
        Page<Book> books = bookRepository.findAll(spec, pageable);

        return ApiResponse.<Page<Book>>builder()
                .data(books)
                .message("Books retrieved successfully.")
                .status(HttpStatus.OK.value())
                .build();
    }

    public ApiResponse<Book> findById(Long id) {
        Book book = findBookById(id);

        return ApiResponse.<Book>builder()
                .data(book)
                .message(String.format("Book with id \"%d\" found successfully.", id))
                .status(HttpStatus.OK.value())
                .build();

    }

    @Transactional
    public ApiResponse<Void> editInfo(Long id, EditBookInfoRequest request) {
        Book book = findBookById(id);
        book.setTitle(request.getTitle());
        book.setAuthorName(request.getAuthorName());
        book.setPublishYear(request.getPublishYear());

        return ApiResponse.success("Book information updated successfully.", HttpStatus.OK);
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        Book book = findBookById(id);
        book.setAvailable(false);
        book.setDeleted(true);

        return ApiResponse.success("Book deleted successfully and no longer available in the library.", HttpStatus.OK);
    }

    private Book findBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new NotFoundException(String.format("Book with id \"%d\" not found.", id));
        }

        return optionalBook.get();
    }
}
