package ru.practicum.ewm.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dao.CommentRepository;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentMapper;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto postComment(int userId, int eventId, NewCommentDto newCommentDto) {
        Comment commentCheck = commentRepository.findByUserId_IdAndEventId(userId, eventId);

        if (commentCheck != null) {
            throw new BadRequestException("You can leave only one comment to this Event!");
        }

        // собираем коммент для сохранения в базе
        Comment comment = CommentMapper.toComment(newCommentDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found!"));
        comment.setUserId(user);
        comment.setEventId(eventId);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }


    @Override
    public CommentDto updateCommentById(int userId, int eventId, int comId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment is not found!"));

        if (comment.getUserId().getId() != userId) {
            throw new BadRequestException("Comment is not yours!");
        }

        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    public void deleteCommentById(int userId, int eventId, int comId) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment is not found!"));

        if (comment.getUserId().getId() != userId) {
            throw new BadRequestException("Comment is not yours!");
        }

        commentRepository.delete(comment);
    }


    @Override
    public CommentDto getCommentById(int comId) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment is not found!"));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(int eventId) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
