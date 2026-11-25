package ut.ac.ir.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.ac.ir.library.dto.request.EditBookInfoRequest;
import ut.ac.ir.library.dto.request.InsertBookRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.entity.Book;
import ut.ac.ir.library.service.BookService;

import java.awt.print.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {
    private final BookService bookService;

    @PostMapping("/insert")
    public ResponseEntity<ApiResponse<Void>> insertBook(@Valid @RequestBody InsertBookRequest request) {
        ApiResponse<Void> response = bookService.insertBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<Page<Book>>> getAllBooks(
            @RequestParam(required = false) String tile,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer publishYear,
            @RequestParam(required = false) Boolean isAvailable,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
            ) {
        ApiResponse<Page<Book>> response = bookService.getAllWithFilters(tile, author, publishYear, isAvailable, pageable);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable Long id) {
        ApiResponse<Book> response = bookService.findById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> editInfo(
            @PathVariable Long id,
            @Valid @RequestBody EditBookInfoRequest request) {
        ApiResponse<Void> response = bookService.editInfo(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ApiResponse<Void> response = bookService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
