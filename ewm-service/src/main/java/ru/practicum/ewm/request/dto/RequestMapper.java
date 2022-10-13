package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.Request;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEventId().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
