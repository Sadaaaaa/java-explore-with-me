package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

public interface CompilationAdminService {
    CompilationDto createNewCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(int compId);

    void deleteEventByIdFromCompilation(int compId, int eventId);

    void addEventToCompilation(int compId, int eventId);

    void unpinCompilation(int compId);

    void pinCompilation(int compId);
}
