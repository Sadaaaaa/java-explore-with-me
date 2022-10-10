package ru.practicum.ewm.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequester_Id(int userId);

    Request findByIdAndRequester_Id(int requestId, int userId);

    List<Request> findAllByEventId_IdAndStatus(int eventId, RequestStatus status);

    Request findByEventId_IdAndId(int eventId, int reqId);

    Request findByEventId_IdAndRequester_Id(int eventId, int userId);

    List<Request> findAllByEventId_IdAndEventId_Initiator_Id(int eventId, int userId);
}
