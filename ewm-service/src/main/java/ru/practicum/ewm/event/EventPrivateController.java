package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventPrivateService;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@CrossOrigin(origins = "*")
public class EventPrivateController {

    private final EventPrivateService eventPrivateService;

    @Autowired
    public EventPrivateController(EventPrivateService eventPrivateService) {
        this.eventPrivateService = eventPrivateService;
    }

    @GetMapping
    public ResponseEntity<?> getUserEvents(@PathVariable int userId,
                                           @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return ResponseEntity.ok(eventPrivateService.getUserEvents(userId, from, size));
    }

    @PatchMapping
    public ResponseEntity<?> updateUserEvents(@PathVariable int userId,
                                              @RequestBody UpdateEventRequest updateEventRequest) {
        return ResponseEntity.ok(eventPrivateService.updateUserEvents(userId, updateEventRequest));
    }

    @PostMapping
    public ResponseEntity<?> createUserEvent(@PathVariable int userId,
                                             @RequestBody NewEventDto newEventDto) {
        return ResponseEntity.ok(eventPrivateService.createUserEvent(userId, newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getUserEventById(@PathVariable int userId,
                                              @PathVariable int eventId) {
        return ResponseEntity.ok(eventPrivateService.getUserEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<?> cancelUserEventById(@PathVariable int userId,
                                                 @PathVariable int eventId) {
        return ResponseEntity.ok(eventPrivateService.cancelUserEventById(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<?> findRequestsByUserIdByEventId(@PathVariable int userId,
                                                           @PathVariable int eventId) {
        return ResponseEntity.ok(eventPrivateService.findRequestsByUserIdByEventId(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<?> confirmRequestFromOtherUser(@PathVariable int userId,
                                                         @PathVariable int eventId,
                                                         @PathVariable int reqId) {
        return ResponseEntity.ok(eventPrivateService.confirmRequestFromOtherUser(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<?> rejectRequestFromOtherUser(@PathVariable int userId,
                                                        @PathVariable int eventId,
                                                        @PathVariable int reqId) {
        return ResponseEntity.ok(eventPrivateService.rejectRequestFromOtherUser(userId, eventId, reqId));
    }
}
