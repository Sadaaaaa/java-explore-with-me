package ru.practicum.ewm.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.statistics.StatClient;
import ru.practicum.ewm.util.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Autowired
    public EventPublicServiceImpl(EventRepository eventRepository, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.statClient = statClient;
    }

    @Override
    public List<EventFullDto> getEventsWithFiltering(String text, List<Integer> categories, Boolean paid,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                              String sort, Integer from, Integer size, String ip, String path) {

        List<Event> searchedEvents = eventRepository.search(text, PageRequest.of(from, size));
        List<Event> searchedEventsInCategories = eventRepository.searchInCategories(text, categories, PageRequest.of(from, size));
        List<Event> searchedInCategoriesAndIsPaid = eventRepository.searchInCategoriesAndIsPaid(text, categories, paid, PageRequest.of(from, size));
//        List<Event> searchedInCategoriesAndIsPaidAndStartDate = eventRepository.searchInCategoriesAndIsPaidAndStartDate();
        List<Event> eventsWithParameters = eventRepository.getEventsWithParameters(text, categories, paid,
                rangeStart, rangeEnd, sort, PageRequest.of(from, size));

        // отправляем статистику на сервер
        EndpointHit endpointHit = new EndpointHit(
                null,
                "Explore with me",
                path,
                ip,
                LocalDateTime.now()
        );
        statClient.sendStatistic(endpointHit);

        return eventsWithParameters.stream().map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Integer eventId, String ip, String path) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not exist!"));

        // событие должно быть опубликовано
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new BadRequestException("Event is not published yet.");
        }

        EndpointHit endpointHit = new EndpointHit(
                null,
                "Explore with me",
                path,
                ip,
                LocalDateTime.now()
        );
        statClient.sendStatistic(endpointHit);


        return EventMapper.fromEventToEventFullDto(event);
    }


}
