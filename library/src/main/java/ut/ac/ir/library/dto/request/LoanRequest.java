package ut.ac.ir.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {
    @NotNull(message = "book id is required.")
    private Long id;

    @NotBlank(message = "borrower name is required.")
    private String borrowerName;
}
