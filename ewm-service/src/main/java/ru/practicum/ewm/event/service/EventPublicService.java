package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventFullDto> getEventsWithFiltering(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                              String ip, String path);

    EventFullDto getEventById(Integer eventId, String ip, String path);
}
