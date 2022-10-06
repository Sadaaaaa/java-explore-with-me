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

    @Query("select i from Event i " +
            "where lower(i.annotation) like concat('%', lower(:search), '%') " +
            " or lower(i.description) like concat('%', lower(:search), '%') ")
    List<Event> search(@Param("search") String textSearch, Pageable pageable);

    @Query("select i from Event i " +
            " where i.category.id in :categories " +
            " and lower(i.annotation) like concat('%', lower(:search), '%') " +
            " or lower(i.description) like concat('%', lower(:search), '%') ")
    List<Event> searchInCategories(@Param("search") String textSearch, @Param("categories") List<Integer> categories, Pageable pageable);

    @Query("select i from Event i " +
            " where i.category.id in :categories " +
            " and lower(i.annotation) like concat('%', lower(:search), '%') " +
            " or lower(i.description) like concat('%', lower(:search), '%') " +
            " and i.paid = :paid")
    List<Event> searchInCategoriesAndIsPaid(@Param("search") String textSearch,
                                            @Param("categories") List<Integer> categories,
                                            @Param("paid") Boolean paid,
                                            Pageable pageable);

    @Query("select i from Event i " +
            " where i.category.id in :categories " +
            " and lower(i.annotation) like concat('%', lower(:search), '%') " +
            " or lower(i.description) like concat('%', lower(:search), '%') " +
            " and i.paid = :paid " +
            " and (i.eventDate >= :rangeStart and i.eventDate <= :rangeEnd) ")
    List<Event> searchInCategoriesAndIsPaidAndStartDate(@Param("search") String textSearch,
                                                        @Param("categories") List<Integer> categories,
                                                        @Param("paid") Boolean paid,
                                                        @Param("rangeStart") LocalDateTime rangeStart,
                                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                                        Pageable pageable);

    @Query("select i from Event i " +
            " where i.category.id in :categories " +
            " and lower(i.annotation) like concat('%', lower(:search), '%') " +
            " or lower(i.description) like concat('%', lower(:search), '%') " +
            " and i.paid = :paid " +
            " and (i.eventDate >= :rangeStart and i.eventDate <= :rangeEnd) " +
            " and i.state = 'PUBLISHED' " +
            " order by :sort ")
    List<Event> getEventsWithParameters(@Param("search") String textSearch,
                                        @Param("categories") List<Integer> categories,
                                        @Param("paid") Boolean paid,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        @Param("sort") String sort,
                                        Pageable pageable);

    Event findByInitiator_IdAndId(int userId, int eventId);

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
