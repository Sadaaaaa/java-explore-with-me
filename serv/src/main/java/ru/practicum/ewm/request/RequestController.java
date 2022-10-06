package ru.practicum.ewm.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.service.RequestService;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<?> getUserRequests(@PathVariable int userId) {
        return ResponseEntity.ok(requestService.getUserRequests(userId));
    }

    @PostMapping
    public ResponseEntity<?> createUserRequest(@PathVariable int userId,
                                               @RequestParam int eventId) {
        return ResponseEntity.ok(requestService.createUserRequest(userId, eventId));
    }

    @PatchMapping("{requestId}/cancel")
    public ResponseEntity<?> cancelUserRequest(@PathVariable int userId,
                                               @PathVariable int requestId) {
        return ResponseEntity.ok(requestService.cancelUserRequest(userId, requestId));
    }



}
