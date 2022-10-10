package ru.practicum.ewm.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.service.CompilationService;

@RestController
@RequestMapping(path = "/compilations")
@CrossOrigin(origins = "*")
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public ResponseEntity<?> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                             @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("{compId}")
    public ResponseEntity<?> getCompilationById(@PathVariable int compId) {
        return ResponseEntity.ok(compilationService.getCompilationById(compId));
    }

}
