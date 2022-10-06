package ru.practicum.ewm.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepository;
    private final EventPublicService eventPublicService;
    private  final CategoryPublicService categoryPublicService;

    @Autowired
    public EventAdminServiceImpl(EventRepository eventRepository, EventPublicService eventPublicService, CategoryPublicService categoryPublicService) {
        this.eventRepository = eventRepository;
        this.eventPublicService = eventPublicService;
        this.categoryPublicService = categoryPublicService;
    }

    public List<EventFullDto> findEventsWithParams(List<Integer> users, List<EventStatus> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        List<Event> searchedEvents = eventRepository.searchEventWithParams(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from, size));

        return searchedEvents.stream().map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(int eventId, AdminUpdateEventRequest update) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));
//        Event event = eventPublicService.getEventById(eventId);
        Event updatedEvent = updateEvent(update, event);
        Event savedEvent = eventRepository.save(updatedEvent);
        return EventMapper.fromEventToEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto publishEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));

        // дата начала события должна быть не ранее чем за час от даты публикации.
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Event will start less than 1 hour!");
        }

        // событие должно быть в состоянии ожидания публикации
        if (event.getState() != EventStatus.PENDING) {
            throw new BadRequestException("Event is not in pending state!");
        }

        event.setState(EventStatus.PUBLISHED);
        event = eventRepository.save(event);
        return EventMapper.fromEventToEventFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));

        // Обратите внимание: событие не должно быть опубликовано.
        if (event.getState() == EventStatus.PUBLISHED) {
            throw new BadRequestException("Event is already published! You can't reject event.");
        }

        event.setState(EventStatus.CANCELED);
        event.setPublishedOn(LocalDateTime.now());

        event = eventRepository.save(event);
        return EventMapper.fromEventToEventFullDto(event);
    }

    // utils
    public Event updateEvent(AdminUpdateEventRequest update, Event event) {

        if (update.getAnnotation() != null) event.setAnnotation(update.getAnnotation());
        if (update.getCategory() != null) event.setCategory(categoryPublicService.getCategoryById(update.getCategory()));
        if (update.getDescription() != null) event.setDescription(update.getDescription());
        if (update.getEventDate() != null) event.setEventDate(update.getEventDate());
        if (update.getLocation() != null) event.setLocation(update.getLocation());
        if (update.getPaid() != null) event.setPaid(update.getPaid());
        if (update.getParticipantLimit() != null) event.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() != null) event.setRequestModeration(update.getRequestModeration());
        if (update.getTitle() != null) event.setTitle(update.getTitle());

        return event;
    }

}
