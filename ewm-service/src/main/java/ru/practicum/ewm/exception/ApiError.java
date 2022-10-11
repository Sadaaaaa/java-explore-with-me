package ru.practicum.ewm.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    private List<String> errors;
    private HttpStatus status;
    private String message;
    private String reason;
    private LocalDateTime timestamp;
}
