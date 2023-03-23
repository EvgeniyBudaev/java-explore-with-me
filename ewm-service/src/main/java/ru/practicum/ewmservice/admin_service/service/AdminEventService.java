package ru.practicum.ewmservice.admin_service.service;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.EventFullDto;
import ru.practicum.ewmservice.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AdminEventService {

    List<Event> getEvents(List<Long> users,
                          List<EventState> states,
                          List<Long> categories,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          Integer from,
                          Integer size);


    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<Event> findEventsFrom(List<Long> eventIds);
}
