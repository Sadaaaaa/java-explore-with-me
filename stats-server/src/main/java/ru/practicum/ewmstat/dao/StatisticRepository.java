package ru.practicum.ewmstat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<EndpointHit, Integer> {

    @Query(" select new ru.practicum.ewmstat.dto.ViewStats(s.app, s.uri, count(s.uri)) from EndpointHit s" +
            " where s.uri = :uris " +
            " and (s.timestamp >= :start and s.timestamp <= :end) " +
            " group by s.app, s.uri ")
    List<ViewStats> findHitsWithParams(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") List<String> uris);

}
