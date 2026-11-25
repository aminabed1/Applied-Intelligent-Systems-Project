package ut.ac.ir.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequest {
    @NotNull(message = "book id is required.")
    private Long bookId;

    @NotBlank(message = "borrower name is required.")
    private String borrowerName;

    @NotNull(message = "loan date is required.")
    private LocalDate loanDate;

    @NotNull(message = "return date is required.")
    private LocalDate returnDate;
}
