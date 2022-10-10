package ru.practicum.ewm.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final CategoryPublicService categoryPublicService;
    private final RequestRepository requestRepository;
    private final EventPublicService eventPublicService;

    @Autowired
    public EventPrivateServiceImpl(EventRepository eventRepository, UserRepository userRepository, UserService userService,
                                   CategoryRepository categoryRepository, CategoryPublicService categoryPublicService, RequestRepository requestRepository, EventPublicService eventPublicService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.categoryPublicService = categoryPublicService;
        this.requestRepository = requestRepository;
        this.eventPublicService = eventPublicService;
    }

    @Override
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        List<Event> usersEvents = eventRepository.findAllByInitiator_Id(userId, PageRequest.of(from, size));
        return usersEvents.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateUserEvents(int userId, UpdateEventRequest updateEventRequest) {
        Event eventToUpdate = eventRepository.findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new NotFoundException("Event is not found!"));

        if (eventToUpdate.getInitiator().getId() != userId) {
            throw new BadRequestException("Event update is not allowed: Wrong User!");
        }

        // изменить можно только отмененные события или события в состоянии ожидания модерации
        if (eventToUpdate.getState() == EventStatus.PUBLISHED) {
            throw new BadRequestException("Event update is not allowed: Event already published!");
        }

        // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Event update is not allowed: Event start less then 2 hours!");
            }
        }

        // если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (eventToUpdate.getState() == EventStatus.CANCELED) {
            eventToUpdate.setState(EventStatus.PENDING);
        }

        Event updatedEvent = updateEvent(eventToUpdate, updateEventRequest);
        Event savedEvent = eventRepository.save(updatedEvent);
        return EventMapper.fromEventToEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto createUserEvent(int userId, NewEventDto newEventDto) {
        // дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (newEventDto.getEventDate() != null) {
            if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Event create is not allowed: Event start less then 2 hours!");
            }
        }
        Event event = EventMapper.fromNewEventDtoToEvent(newEventDto);
        // add user to new event
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found!"));
        event.setInitiator(user);

        // add category to new event
        if (newEventDto.getCategory() != null) event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category is not found!")));
        Event savedEvent = eventRepository.save(event);

        return EventMapper.fromEventToEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getUserEventById(int userId, int eventId) {
//        Event event = eventRepository.findByInitiator_IdAndId(userId, eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Item is not found!"));

        if (event.getInitiator().getId() != userId) {
            throw new BadRequestException("Event get is not allowed: Wrong User!");
        }

        return EventMapper.fromEventToEventFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEventById(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));

        if (event.getInitiator().getId() != userId) {
            throw new BadRequestException("Event cancel is not allowed: Wrong User!");
        }

        // отменить можно только событие в состоянии ожидания модерации
        if (event.getState() != EventStatus.PENDING) {
            throw new BadRequestException("Event cancel is not allowed: Event is not on moderation!");
        }

        event.setState(EventStatus.CANCELED);
        Event canceledEvent = eventRepository.save(event);
        return EventMapper.fromEventToEventFullDto(canceledEvent);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserIdByEventId(int userId, int eventId) {

        List<Request> requests = requestRepository.findAllByEventId_IdAndEventId_Initiator_Id(eventId, userId);


        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequestFromOtherUser(int userId, int eventId, int reqId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));

        // если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new BadRequestException("Request moderation is false OR participant limit is 0.");
        }

        // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            throw new BadRequestException("Participant limit reached maximum participants.");
        }

        Request request = requestRepository.findByEventId_IdAndId(eventId, reqId);
        request.setStatus(RequestStatus.CONFIRMED);
        Request confirmedRequest = requestRepository.save(request);

        // обновляем счетчик подтвержденных заявок
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);

        // если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            List<Request> requests = requestRepository.findAllByEventId_IdAndStatus(eventId, RequestStatus.PENDING);
            requests.forEach(req -> req.setStatus(RequestStatus.CANCELED));
        }

        return RequestMapper.toParticipationRequestDto(confirmedRequest);
    }

    @Override
    public ParticipationRequestDto rejectRequestFromOtherUser(int userId, int eventId, int reqId) {
        Request request = requestRepository.findByEventId_IdAndId(eventId, reqId);
        Event requestedEvent = request.getEventId();

        // проверяем, принадлежит ли ивент юзеру, отменяющему реквест
        if (requestedEvent.getInitiator().getId() != userId) {
            throw new BadRequestException("Wrong event initiator ID!");
        }
        request.setStatus(RequestStatus.REJECTED);
        Request confirmedRequest = requestRepository.save(request);

        // обновляем счетчик подтвержденных заявок
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        return RequestMapper.toParticipationRequestDto(confirmedRequest);
    }


    // utils
    public Event updateEvent(Event event, UpdateEventRequest updateEventRequest) {

        if (updateEventRequest.getAnnotation() != null) event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getCategory() != null)
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category is not found!")));
        if (updateEventRequest.getDescription() != null) event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getEventDate() != null) event.setEventDate(updateEventRequest.getEventDate());
        if (updateEventRequest.getPaid() != null) event.setPaid(updateEventRequest.getPaid());
        if (updateEventRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        if (updateEventRequest.getTitle() != null) event.setTitle(updateEventRequest.getTitle());

        return event;
    }
}
