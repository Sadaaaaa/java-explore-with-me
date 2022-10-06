package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    List<EventFullDto> findEventsWithParams(List<Integer> users, List<EventStatus> states, List<Integer> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(int eventId, AdminUpdateEventRequest update);

    EventFullDto publishEvent(int eventId);

    EventFullDto rejectEvent(int eventId);
}
