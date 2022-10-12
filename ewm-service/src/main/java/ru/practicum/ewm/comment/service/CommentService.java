package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto postComment(int userId, int eventId, NewCommentDto newCommentDto);

    CommentDto getCommentById(int comId);

    CommentDto updateCommentById(int userId, int eventId, int comId, NewCommentDto newCommentDto);

    void deleteCommentById(int userId, int eventId, int comId);

    List<CommentDto> getAllCommentsByEventId(int eventId);
}
