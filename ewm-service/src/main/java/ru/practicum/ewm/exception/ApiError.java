package ru.practicum.ewm.exception;

import lombok.*;

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
    private ErrorStatus status;
    private String message;
    private String reason;
    private LocalDateTime timestamp;
}
