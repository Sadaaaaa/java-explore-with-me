package ru.practicum.ewm.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

@RestController
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    // Private methods
    @PostMapping("/users/{userId}/events/{eventId}/comment")
    public ResponseEntity<?> postComment(@PathVariable int userId,
                                         @PathVariable int eventId,
                                         @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.ok(commentService.postComment(userId, eventId, newCommentDto));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comment/{comId}")
    public ResponseEntity<?> updateCommentById(@PathVariable int userId,
                                               @PathVariable int eventId,
                                               @PathVariable int comId,
                                               @RequestBody NewCommentDto newCommentDto) {
        return ResponseEntity.ok(commentService.updateCommentById(userId, eventId, comId, newCommentDto));
    }

    @DeleteMapping("/users/{userId}/events/{eventId}/comment/{comId}")
    public void deleteCommentById(@PathVariable int userId,
                                  @PathVariable int eventId,
                                  @PathVariable int comId) {
        commentService.deleteCommentById(userId, eventId, comId);
    }


    // Public methods
    @GetMapping("comment/{comId}")
    public ResponseEntity<?> getCommentById(@PathVariable int comId) {
        return ResponseEntity.ok(commentService.getCommentById(comId));
    }

    @GetMapping("comment/all/{eventId}")
    public ResponseEntity<?> getAllCommentsByEventId(@PathVariable int eventId) {
        return ResponseEntity.ok(commentService.getAllCommentsByEventId(eventId));
    }

}
