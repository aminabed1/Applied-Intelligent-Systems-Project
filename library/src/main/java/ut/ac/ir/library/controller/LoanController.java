package ut.ac.ir.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.ac.ir.library.dto.request.LoanRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.service.LoanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/loans")
public class LoanController {
    private final LoanService loanService;

    @PutMapping("/insert")
    public ResponseEntity<ApiResponse<Void>> insert(@Valid @RequestBody LoanRequest request) {
        ApiResponse<Void> response = loanService.insert(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<ApiResponse<Void>> returnBook(@PathVariable Long id) {
        ApiResponse<Void> response = loanService.returnBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
