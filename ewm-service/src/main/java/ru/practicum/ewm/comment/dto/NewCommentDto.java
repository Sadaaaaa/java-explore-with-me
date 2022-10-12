package ru.practicum.ewm.comment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NewCommentDto {
    private String text;
}
