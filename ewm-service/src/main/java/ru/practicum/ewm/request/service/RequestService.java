package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(int userId);

    ParticipationRequestDto createUserRequest(int userId, int eventId);

    ParticipationRequestDto cancelUserRequest(int userId, int requestId);
}
