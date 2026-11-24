package ut.ac.ir.library.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;
    private Integer status;
    private Map<String, String> errors;
    private boolean success;
    private String path;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status.value())
                .success(true)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .status(status.value())
                .success(true)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status.value())
                .success(false)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message, HttpStatus status, Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status.value())
                .errors(errors)
                .success(false)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message, T data, HttpStatus status, Map<String, String> errors, String path) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .status(status.value())
                .errors(errors)
                .success(false)
                .path(path)
                .build();
    }
}
