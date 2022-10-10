package ru.practicum.ewm.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.statistics.StatClient;
import ru.practicum.ewm.util.EndpointHit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient statClient;

    @Autowired
    public EventPublicServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.statClient = statClient;
    }

    @Transactional
    @Override
    public List<EventFullDto> getEventsWithFiltering(String text, List<Integer> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                     String sort, Integer from, Integer size, String ip, String path) {

        List<Event> eventsWithParameters;
        List<Boolean> paidParam;

        // если в запросе не указан параметр sort
        if (sort == null) {
            sort = "id";
        } else if (sort.equals("EVENT_DATE")) {
            sort = "eventDate";
        } else if (sort.equals("VIEWS")) {
            sort = "views";
        }

        // если в запросе не указан параметр paid
        if (paid == null) {
            paidParam = List.of(true, false);
        } else {
            paidParam = List.of(paid);
        }

        // если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(999);
        }

        // если категория пуста - то добавляем все категории, которые есть в БД
        if (categories == null) {
            categories = categoryRepository.findAll().stream().map(Category::getId).collect(Collectors.toList());
        }

        // показываются только события у которых не исчерпан лимит запросов на участие, если onlyAvailable=true
        if (onlyAvailable != null && onlyAvailable) {
            eventsWithParameters = eventRepository.getEventsWithParametersAndIfAvailable(text, categories,
                    paidParam, rangeStart, rangeEnd, PageRequest.of(from / size, size, Sort.by(sort)));
        } else {
            eventsWithParameters = eventRepository.getEventsWithParameters(text, categories,
                    paidParam, rangeStart, rangeEnd, PageRequest.of(from / size, size, Sort.by(sort)));
        }

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
