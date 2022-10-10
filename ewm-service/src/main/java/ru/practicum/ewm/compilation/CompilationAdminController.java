package ru.practicum.ewm.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationAdminService;

@RestController
@RequestMapping(path = "/admin/compilations")
@CrossOrigin(origins = "*")
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    @Autowired
    public CompilationAdminController(CompilationAdminService compilationAdminService) {
        this.compilationAdminService = compilationAdminService;
    }

    @PostMapping
    public ResponseEntity<?> createNewCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity.ok(compilationAdminService.createNewCompilation(newCompilationDto));
    }

    @DeleteMapping("{compId}")
    public void deleteCompilationById(@PathVariable int compId) {
        compilationAdminService.deleteCompilationById(compId);
    }

    @DeleteMapping("{compId}/events/{eventId}")
    public void deleteEventByIdFromCompilation(@PathVariable int compId,
                                               @PathVariable int eventId) {
        compilationAdminService.deleteEventByIdFromCompilation(compId, eventId);
    }

    @PatchMapping("{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId,
                                      @PathVariable int eventId) {
        compilationAdminService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("{compId}/pin")
    public void unpinCompilation(@PathVariable int compId) {
        compilationAdminService.unpinCompilation(compId);
    }

    @PatchMapping("{compId}/pin")
    public void pinCompilation(@PathVariable int compId) {
        compilationAdminService.pinCompilation(compId);
    }

}
