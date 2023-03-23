package ru.practicum.ewmservice.private_service.service;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.*;
import java.util.List;

@Service
public interface PrivateEventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId,
                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
