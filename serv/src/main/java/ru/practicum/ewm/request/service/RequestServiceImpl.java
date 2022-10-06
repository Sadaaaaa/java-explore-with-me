package ru.practicum.ewm.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventPublicService eventPublicService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository, EventPublicService eventPublicService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventPublicService = eventPublicService;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(int userId) {
        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createUserRequest(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found!"));

        // нельзя добавить повторный запрос
        if (requestRepository.findByEventId_IdAndRequester_Id(eventId, userId) != null) {
            throw new NotFoundException("Request is already exist!");
        }

        // инициатор события не может добавить запрос на участие в своём событии
        if (userId == event.getInitiator().getId()) {
            throw new NotFoundException("Request to own event is not possible!");
        }

        // нельзя участвовать в неопубликованном событии
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new NotFoundException("Event is not published yet!");
        }

        // если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new NotFoundException("Reached max limit of requests!");
        }



        Request request = new Request(
                null,
                LocalDateTime.now(),
                event,
                user,
                RequestStatus.PENDING
        );

        // если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        Request requestFromDatabase = requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(requestFromDatabase);
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(int userId, int requestId) {
        Request requestForCancel = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (requestForCancel == null) {
            throw new NotFoundException("Request is not found!");
        }

        requestForCancel.setStatus(RequestStatus.CANCELED);
        Request canceledRequest = requestRepository.save(requestForCancel);

        return RequestMapper.toParticipationRequestDto(canceledRequest);
    }


}
