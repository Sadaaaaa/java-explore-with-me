package ru.practicum.ewmstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmstat.model.EndpointHit;
import ru.practicum.ewmstat.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping("/hit")
    public void saveStatInfo(@RequestBody EndpointHit endpointHit) {
        statisticService.saveStatInfo(endpointHit);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStatInfo(
                                         @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam(value = "uris", required = false) List<String> uris,
                                         @RequestParam(value = "unique", required = false) Boolean unique) {
        return ResponseEntity.ok(statisticService.getStatInfo(start, end, uris, unique));
    }
}
