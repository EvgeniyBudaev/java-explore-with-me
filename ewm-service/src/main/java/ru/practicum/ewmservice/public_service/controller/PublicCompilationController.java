package ru.practicum.ewmservice.public_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.public_service.service.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable @PositiveOrZero Long compId) {
        log.info("GET: /compilations/{}", compId);
        return ResponseEntity.ok().body(publicCompilationService.getCompilation(compId));
    }

    @GetMapping()
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(value = "from", defaultValue = "0")
                                                @PositiveOrZero Integer from,
                                                @RequestParam(value = "size", defaultValue = "10")
                                                @Positive Integer size) {
        log.info("GET: /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return ResponseEntity.ok().body(publicCompilationService.getCompilations(pinned, from, size));
    }
}
