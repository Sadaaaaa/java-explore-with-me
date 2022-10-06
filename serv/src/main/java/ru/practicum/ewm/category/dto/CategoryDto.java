package ru.practicum.ewm.category.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private int id;
    private String name;
}
