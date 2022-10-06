package ru.practicum.ewmstat.dto;

import ru.practicum.ewmstat.model.EndpointHit;

public class StatMapper {

    public static ViewStats toViewStats(EndpointHit endpointHit) {
        return new ViewStats(
                endpointHit.getApp(),
                endpointHit.getUri(),
                null
        );
    }
}
