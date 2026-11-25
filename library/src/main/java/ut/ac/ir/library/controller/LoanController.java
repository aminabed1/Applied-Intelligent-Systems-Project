package ut.ac.ir.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.service.LoanService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/loans")
public class LoanController {
    private final LoanService loanService;

    @PutMapping("/insert")
    public ResponseEntity<ApiResponse<Void>> insert() {

    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<ApiResponse<Void>> returnBook() {

    }

}
