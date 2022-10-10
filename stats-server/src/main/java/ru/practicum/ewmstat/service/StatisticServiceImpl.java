package ru.practicum.ewmstat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewmstat.dao.StatisticRepository;
import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void saveStatInfo(EndpointHit endpointHit) {
        statisticRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStatInfo(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> stat = statisticRepository.findHitsWithParams(start, end, uris);


        return stat;
    }
}
