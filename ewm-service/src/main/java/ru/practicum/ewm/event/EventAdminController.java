package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.service.EventAdminService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@CrossOrigin(origins = "*")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    @Autowired
    public EventAdminController(EventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    @GetMapping
    public ResponseEntity<?> findEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                        @RequestParam(value = "states", required = false) List<EventStatus> states,
                                        @RequestParam(value = "categories", required = false) List<Integer> categories,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(value = "rangeStart", required = false) LocalDateTime rangeStart,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
                                        @RequestParam(value = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                        @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventAdminService.findEventsWithParams(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable int eventId,
                                         @RequestBody AdminUpdateEventRequest update) {
        return ResponseEntity.ok(eventAdminService.updateEvent(eventId, update));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<?> publishEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(eventAdminService.publishEvent(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<?> rejectEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(eventAdminService.rejectEvent(eventId));
    }

}
