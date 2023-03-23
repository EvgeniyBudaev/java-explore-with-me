package ru.practicum.ewmservice.private_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.private_service.service.PrivateRequestService;
import java.util.List;

@RequestMapping("users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @PostMapping()
    public ResponseEntity<ParticipationRequestDto> postRequest(@PathVariable Long userId,
                                               @RequestParam Long eventId) {
        log.info("POST: /users/{}/requests?eventId={}", userId, eventId);
        return ResponseEntity.status(201).body(privateRequestService.createRequest(userId, eventId));
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId) {
        log.info("GET: /users/{}/requests", userId);
        return ResponseEntity.ok().body(privateRequestService.getRequests(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("PATCH: /users/{}/requests/{}/cancel", userId, requestId);
        return ResponseEntity.ok().body(privateRequestService.cancelRequest(userId, requestId));
    }

}
