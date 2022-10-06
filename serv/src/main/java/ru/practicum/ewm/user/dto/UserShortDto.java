package ru.practicum.ewm.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {
    private Integer id;
    private String name;
}
