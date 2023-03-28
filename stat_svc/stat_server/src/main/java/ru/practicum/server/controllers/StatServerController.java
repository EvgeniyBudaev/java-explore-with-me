package ru.practicum.server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.constants.Pattern;
import ru.practicum.server.services.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatServerController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        statService.saveHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@DateTimeFormat(pattern = Pattern.DATE)
                                       @RequestParam(value = "start") LocalDateTime start,
                                       @DateTimeFormat(pattern = Pattern.DATE)
                                       @RequestParam(value = "end") LocalDateTime end,
                                       @RequestParam List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
