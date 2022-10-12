package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event fromNewEventDtoToEvent(NewEventDto newEventDto) {
        return new Event(
                null,
                newEventDto.getAnnotation(),
                null,
                0,
                LocalDateTime.now(), // фиксируем время создания ивента
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                null,
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                EventStatus.PENDING,
                newEventDto.getTitle(),
                0,
                null
        );
    }

    public static EventFullDto fromEventToEventFullDto(Event event) {
        List<CommentDto> comments = new ArrayList<>();
        if (event.getComments() != null) {
            comments = event.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        }

        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews(),
                comments
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        List<CommentDto> comments = new ArrayList<>();
        if (event.getComments() != null) {
            comments = event.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        }
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews(),
                comments
        );
    }
}
