package ru.practicum.ewm.exception;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private ErrorStatus status;
    private String timestamp;
}
