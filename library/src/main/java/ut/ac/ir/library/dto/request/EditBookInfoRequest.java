package ut.ac.ir.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EditBookInfoRequest {

    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters.")
    private String title;

    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters.")
    private String authorName;

    @Min(value = 1000, message = "Invalid publish year.")
    @Max(value = 2026, message = "Publish year cannot be in the future.")
    private Integer publishYear;
}
