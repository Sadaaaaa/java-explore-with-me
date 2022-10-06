package ru.practicum.ewm.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dao.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationService compilationService;
    private final EventPublicService eventPublicService;

    @Autowired
    public CompilationAdminServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository, CompilationService compilationService, EventPublicService eventPublicService) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.compilationService = compilationService;
        this.eventPublicService = eventPublicService;
    }

    @Override
    public CompilationDto createNewCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        log.warn("compil" + compilation);
        List<Event> eventsForCompilation = eventRepository.findAllByIds(newCompilationDto.getEvents());
        compilation.setEvents(eventsForCompilation);

        Compilation compilationUpdated = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationDto(compilationUpdated);
    }

    @Override
    public void deleteCompilationById(int compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventByIdFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found!"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));
//        Event event = eventPublicService.getEventById(eventId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found!"));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found!"));
//        Event event = eventPublicService.getEventById(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found!"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found!"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }


}
