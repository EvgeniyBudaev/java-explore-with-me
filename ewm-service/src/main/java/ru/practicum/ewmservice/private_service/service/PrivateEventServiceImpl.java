package ru.practicum.ewmservice.private_service.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dto.*;
import ru.practicum.ewmservice.exceptions.ConflictException;
import ru.practicum.ewmservice.exceptions.ForbiddenException;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.mapper.EventMapper;
import ru.practicum.ewmservice.mapper.RequestMapper;
import ru.practicum.ewmservice.model.*;
import ru.practicum.ewmservice.repository.CategoryRepository;
import ru.practicum.ewmservice.repository.EventRepository;
import ru.practicum.ewmservice.repository.RequestRepository;
import ru.practicum.ewmservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;

    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto createRequest) {
        User user = findUserEntity(userId);
        Category category = findCategoryEntity(createRequest.getCategory());
        Event createdEvent = eventMapper.toEvent(createRequest);
        validateEventDate(createdEvent.getEventDate());
        createdEvent.setInitiator(user);
        createdEvent.setCategory(category);
        return eventMapper.toEventFullDto(eventRepository.save(createdEvent));
    }

    private User findUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundValidationException("User with id: " + id + " not found")
        );
    }

    private Category findCategoryEntity(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundValidationException("Category with id: " + id + " was not found")
        );
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        verifyUserExists(userId);
        Event foundEvent = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(()
                -> new NotFoundValidationException("Event with id: " + eventId + " not found"));
        return eventMapper.toEventFullDto(foundEvent);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        verifyUserExists(userId);
        if (updateEventUserRequest.getEventDate() != null) {
            validateEventDate(updateEventUserRequest.getEventDate());
        }
        Event savedEvent = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundValidationException("Event with id: " + eventId + " not found"));

        if (!savedEvent.getInitiator().getId().equals(userId)) {
            throw new ConflictException("User with id: " + userId + "have not initiated event with id: "
                    + eventId + " not found");
        }
        if (savedEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event is not Pending");
        }
        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(getEventUpdate(updateEventUserRequest, savedEvent));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Event title is a duplicate");
        }
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        verifyUserExists(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.findEventsByInitiatorIdOrderById(userId, pageable);
        return eventMapper.toEventShortDtoList(eventList);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        verifyUserExists(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundValidationException("Event with id: " + eventId + " not found"));
        validateEventInitiator(event, userId);
        List<ParticipationRequest> requests = requestRepository.findByEventId(eventId);
        return requestMapper.toParticipationRequestDtoList(requests);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        verifyUserExists(userId);
        Event event = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(()
                -> new NotFoundValidationException("Event with id: " + eventId + "have not initiated user with id: "
                + userId + " not found"));
        List<ParticipationRequest> requestsForUpdate = requestRepository
                .findRequestsForUpdate(eventId, eventRequestStatusUpdateRequest.getRequestIds());
        return updateAndSaveRequests(requestsForUpdate, eventRequestStatusUpdateRequest.getStatus(), event);
    }

    private EventRequestStatusUpdateResult updateAndSaveRequests(List<ParticipationRequest> requestsForUpdate, StateRequestStatus status, Event event) {
        switch (status) {
            case CONFIRMED:
                return confirmRequests(requestsForUpdate, event);
            case REJECTED:
                return rejectRequests(requestsForUpdate);
            default:
                return new EventRequestStatusUpdateResult();
        }
    }

    private EventRequestStatusUpdateResult confirmRequests(List<ParticipationRequest> requestsForUpdate, Event event) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        for (ParticipationRequest request : requestsForUpdate) {
            try {
                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ConflictException("Request status should be PENDING");
                }
            } catch (ConflictException e) {
                continue;
            }
            if (event.getParticipantLimit() - event.getConfirmedRequests() <= 0) {
                throw new ConflictException("Confirm failed");
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
                ParticipationRequest confirmedRequest = requestRepository.save(request);
                result.getConfirmedRequests().add(requestMapper.toParticipationRequestDto(confirmedRequest));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.setEventConfirmedRequests(event.getId(), event.getConfirmedRequests());
            }
        }
        return result;
    }

    private EventRequestStatusUpdateResult rejectRequests(List<ParticipationRequest> requestsForUpdate) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        for (ParticipationRequest request : requestsForUpdate) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Request status should be PENDING");
            }
            ParticipationRequest rejectedRequest = setRequestRejectedStatus(request);
            result.getRejectedRequests().add(requestMapper.toParticipationRequestDto(rejectedRequest));
        }
        return result;
    }

    private ParticipationRequest setRequestRejectedStatus(ParticipationRequest participationRequest) {
        participationRequest.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(participationRequest);
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundValidationException("User with id: " + userId + " not found");
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        LocalDateTime verificationDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(verificationDate)) {
            throw new ConflictException("Event date should be at least two hours from now in the future");
        }
    }

    private void validateEventInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("Initiator is user");
        }
    }

    private Event getEventUpdate(UpdateEventUserRequest updateEventUserRequest, Event event) {
        UpdateEventUserRequest newUpdateEventUserRequest = eventMapper.toUpdateEventUserRequest(event);
        newUpdateEventUserRequest.setStateAction(updateEventUserRequest.getStateAction());
        if (newUpdateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(newUpdateEventUserRequest.getCategory()).orElseThrow(()
                    -> new NotFoundValidationException("Category with id: "
                    + newUpdateEventUserRequest.getCategory() + " not found"));
            event.setCategory(category);
        }
        switch (newUpdateEventUserRequest.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setState(EventState.CANCELED);
                break;
        }
        return event;
    }
}
