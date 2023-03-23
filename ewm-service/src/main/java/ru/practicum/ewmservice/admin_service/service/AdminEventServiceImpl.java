package ru.practicum.ewmservice.admin_service.service;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.EventFullDto;
import ru.practicum.ewmservice.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.exceptions.ConflictException;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.mapper.EventMapper;
import ru.practicum.ewmservice.model.Category;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;
import ru.practicum.ewmservice.model.QEvent;
import ru.practicum.ewmservice.repository.CategoryRepository;
import ru.practicum.ewmservice.repository.EventRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    public List<Event> findEventsFrom(List<Long> eventIds) {
        return eventRepository.findEventsByIdIn(eventIds);
    }

    @Override
    public List<Event> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        QEvent qEvent = QEvent.event;
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        if (users != null) {
            expression = expression.and(qEvent.initiator.id.in(users));
        }
        if (states != null) {
            expression = expression.and(qEvent.state.in(states));
        }
        if (categories != null) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.before(rangeEnd));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.before(rangeEnd));
        }
        return eventRepository.findAll(expression, page).getContent();
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null) {
            validateEventDate(updateEventAdminRequest.getEventDate());
        }
        Event savedEvent = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundValidationException("Event with id: " + eventId + " not found"));
        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(getEventUpdate(updateEventAdminRequest, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Event title duplicate.");
        }
        return eventMapper.toEventFullDto(updatedEvent);
    }

    private Event getEventUpdate(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        eventUpdateValidation(updateEventAdminRequest, event);
        switch (updateEventAdminRequest.getStateAction()) {
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new ConflictException("State should be Pending");
                }
                break;
            case REJECT_EVENT:
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.CANCELED);
                } else {
                    throw new ConflictException("State should be Pending");
                }
                break;
        }
        return event;
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ConflictException("Event date should be at least two hours from now in the future");
        }
    }

    private void eventUpdateValidation(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(()
                    -> new NotFoundValidationException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(event.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
    }
}

