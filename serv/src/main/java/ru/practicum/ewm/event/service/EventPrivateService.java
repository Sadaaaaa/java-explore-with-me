package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {
    List<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto updateUserEvents(int userId, UpdateEventRequest updateEventRequest);

    EventFullDto createUserEvent(int userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(int userId, int eventId);

    EventFullDto cancelUserEventById(int userId, int eventId);

    List<ParticipationRequestDto> findRequestsByUserIdByEventId(int userId, int eventId);

    ParticipationRequestDto confirmRequestFromOtherUser(int userId, int eventId, int reqId);

    ParticipationRequestDto rejectRequestFromOtherUser(int userId, int eventId, int reqId);
}
