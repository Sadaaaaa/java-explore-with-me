package ru.practicum.ewmstat.service;

import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    void saveStatInfo(EndpointHit endpointHit);

    List<ViewStats> getStatInfo(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
