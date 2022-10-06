package ru.practicum.ewm.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@CrossOrigin(origins = "*")
public class EventPublicController {

    private EventPublicService eventPublicService;

    @Autowired
    public EventPublicController(EventPublicService eventPublicService) {
        this.eventPublicService = eventPublicService;
    }

    @GetMapping
    public ResponseEntity<?> getEventsWithFiltering(@RequestParam(value = "text", required = false) String text,
                                                    @RequestParam(value = "categories", required = false) List<Integer> categories,
                                                    @RequestParam(value = "paid", required = false) Boolean paid,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(value = "rangeStart", required = false) LocalDateTime rangeStart,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(value = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                                    @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                    @RequestParam(value = "sort", required = false) String sort,
                                                    @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        return ResponseEntity.ok(eventPublicService.getEventsWithFiltering(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, ip, path));
    }

    @GetMapping("{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable Integer eventId,
                                          HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        return ResponseEntity.ok(eventPublicService.getEventById(eventId, ip, path));
    }
}
