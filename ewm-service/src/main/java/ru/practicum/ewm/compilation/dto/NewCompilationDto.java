package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    private List<Integer> events;
    private Boolean pinned;
    private String title;
}
