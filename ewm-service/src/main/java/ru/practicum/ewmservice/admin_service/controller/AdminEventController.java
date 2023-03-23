package ru.practicum.ewmservice.admin_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.service.AdminEventService;
import ru.practicum.ewmservice.dto.EventFullDto;
import ru.practicum.ewmservice.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("admin/events")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<EventState> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime rangeEnd,
                                                @RequestParam(value = "from", defaultValue = "0")
                                                     @PositiveOrZero Integer from,
                                                @RequestParam(value = "size", defaultValue = "10")
                                                     @Positive Integer size) {
        log.info("GET: /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok().body(adminEventService.getEvents(users, states, categories,
                rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long eventId,
                                   @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("PATCH: /admin/events/{}", eventId);
        return ResponseEntity.ok().body(adminEventService.updateEvent(eventId, eventDto));
    }
}
