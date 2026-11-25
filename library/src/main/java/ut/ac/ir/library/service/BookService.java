package ut.ac.ir.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ut.ac.ir.library.dto.request.EditBookInfoRequest;
import ut.ac.ir.library.dto.request.InsertBookRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.repository.BookRepository;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public ApiResponse<Void> insertBook(InsertBookRequest request) {

    }

    public ApiResponse<Page<Book>> getAllWithFilters(String title, String authorName, Integer publishYear, Boolean isAvailable, Pageable pageable) {

    }

    public ApiResponse<Book> findById(Long id) {

    }

    public ApiResponse<Void> editInfo(Long id, EditBookInfoRequest request) {

    }

    public ApiResponse<Void> delete(Long id) {

    }
}
