package ru.practicum.ewm.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiator_Id(int userId, Pageable pageable);

    @Query(" select e from Event e " +
            " where e.participantLimit > e.confirmedRequests " +
            " and (lower(e.annotation) like lower(concat('%', :search, '%')) " +
            " or lower(e.description) like lower(concat('%', :search, '%'))) " +
            " and e.category.id in :categories " +
            " and e.paid in :paid " +
            " and (e.eventDate >= :rangeStart and e.eventDate <= :rangeEnd) " +
            " and e.state = 'PUBLISHED' ")
    List<Event> getEventsWithParametersAndIfAvailable(@Param("search") String textSearch,
                                                      @Param("categories") List<Integer> categories,
                                                      @Param("paid") List<Boolean> paid,
                                                      @Param("rangeStart") LocalDateTime rangeStart,
                                                      @Param("rangeEnd") LocalDateTime rangeEnd,
//                                                      @Param("sort") String sort,
                                                      Pageable pageable);

    @Query(" select e from Event e " +
            " where (lower(e.annotation) like lower(concat('%', :search, '%')) " +
            " or lower(e.description) like lower(concat('%', :search, '%'))) " +
            " and e.paid in :paid " +
            " and e.category.id in :categories " +
            " and (e.eventDate >= :rangeStart and e.eventDate <= :rangeEnd) " +
            " and e.state = 'PUBLISHED' ")
    List<Event> getEventsWithParameters(@Param("search") String textSearch,
                                        @Param("categories") List<Integer> categories,
                                        @Param("paid") List<Boolean> paid,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
//                                        @Param("sort") String sort,
                                        Pageable pageable);


    @Query(" select e from Event e " +
            " where e.initiator.id in :users " +
            " and e.state in :states " +
            " and e.category.id in :categories " +
            " and (e.eventDate >= :rangeStart and e.eventDate <= :rangeEnd) ")
    List<Event> searchEventWithParams(@Param("users") List<Integer> users,
                                      @Param("states") List<EventStatus> states,
                                      @Param("categories") List<Integer> categories,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      Pageable pageable);


    @Query(" select e from Event e " +
            " where e.id in :eventIds ")
    List<Event> findAllByIds(List<Integer> eventIds);

    Event findByCategory_Id(int catId);


}
