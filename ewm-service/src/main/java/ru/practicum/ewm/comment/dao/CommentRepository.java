package ru.practicum.ewm.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByEventId(int eventId);

    Comment findByUserId_IdAndEventId(int userId, int eventId);
}
