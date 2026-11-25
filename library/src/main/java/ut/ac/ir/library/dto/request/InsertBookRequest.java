package ut.ac.ir.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InsertBookRequest {

    @NotBlank(message = "Title is required.")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters.")
    private String title;

    @NotBlank(message = "Author's name is required.")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters.")
    private String authorName;

    @NotBlank(message = "ISBN is required.")
    @Pattern(regexp = "^(?:ISBN(?:\\s?-\\d[\\s-]?)?(?:\\d[\\s-]){9}[\\dX]|ISBN(?:\\s?-\\d[\\s-]?)?(?:\\d[\\s-]){12}[\\dX]$)",
            message = "Invalid ISBN format. Please enter a valid ISBN-10 or ISBN-13.")
    private String isbn;

    @NotNull(message = "Publish year is required.")
    @Min(value = 1000, message = "Invalid publish year.")
    @Max(value = 2026, message = "Publish year cannot be in the future.")
    private Integer publishYear;
}
