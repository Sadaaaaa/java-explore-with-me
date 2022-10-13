package ru.practicum.ewm.compilation.dto;

import lombok.*;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    private int id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;

}
