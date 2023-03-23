package ru.practicum.statservice.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.dto.HitDto;
import ru.practicum.statdto.dto.StatDto;
import ru.practicum.statservice.model.Hit;
import ru.practicum.statservice.service.StatService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    public ResponseEntity<HitDto> saveStat(@RequestBody @Valid Hit hit) {
        log.info("POST: /hit");
        return ResponseEntity.status(201).body(service.addHits(hit));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatDto>> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime end,
                                 @RequestParam List<String> uris,
                                 @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET: /stats");
        return ResponseEntity.ok().body(service.getStats(start, end, uris, unique));
    }
}
