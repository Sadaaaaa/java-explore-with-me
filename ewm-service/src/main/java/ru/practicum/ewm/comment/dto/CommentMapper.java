package ru.practicum.ewm.comment.dto;

import ru.practicum.ewm.comment.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUserId(),
                comment.getEventId(),
                comment.getCreated()
        );
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return new Comment(
                null,
                newCommentDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }
}
