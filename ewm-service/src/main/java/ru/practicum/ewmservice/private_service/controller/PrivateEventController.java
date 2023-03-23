package ru.practicum.ewmservice.private_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.*;
import ru.practicum.ewmservice.private_service.service.PrivateEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping("users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable Long userId,
                                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST: /users/{}/events", userId);
        return ResponseEntity.status(201).body(privateEventService.createEvent(userId, newEventDto));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable Long userId,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("GET: /users/{}/events?from={}&size={}", userId, from, size);
    return ResponseEntity.ok().body(privateEventService.getEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("GET: /users/{}/events/{}", userId, eventId);
        return ResponseEntity.ok().body(privateEventService.getEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH: /users/{}/events/{}", userId, eventId);
        return ResponseEntity.ok().body(privateEventService.updateEvent(userId, eventId, updateEventUserRequest));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        log.info("GET: /users/{}/events/{}/requests", userId, eventId);
        return ResponseEntity.ok().body(privateEventService.getEventRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> patchRequests(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH: /users/{}/events/{}/requests/", userId, eventId);
        return ResponseEntity.ok().body(privateEventService
                .updateRequest(userId, eventId, eventRequestStatusUpdateRequest));
    }
}
