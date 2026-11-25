package ut.ac.ir.library.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ut.ac.ir.library.dto.request.LoanRequest;
import ut.ac.ir.library.dto.response.ApiResponse;
import ut.ac.ir.library.repository.LoanRepository;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanService;

    public ApiResponse<Void> insert(LoanRequest request) {

    }

    public ApiResponse<Void> returnBook(Long id) {

    }
}
