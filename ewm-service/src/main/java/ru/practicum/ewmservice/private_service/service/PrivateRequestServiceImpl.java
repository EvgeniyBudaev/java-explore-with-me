package ru.practicum.ewmservice.private_service.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.exceptions.ConflictException;
import ru.practicum.ewmservice.exceptions.ForbiddenException;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.mapper.RequestMapper;
import ru.practicum.ewmservice.model.*;
import ru.practicum.ewmservice.repository.EventRepository;
import ru.practicum.ewmservice.repository.RequestRepository;
import ru.practicum.ewmservice.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        List<ParticipationRequest> foundRequests = requestRepository.findRequestByRequesterId(userId);
        return requestMapper.toParticipationRequestDtoList(foundRequests);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        verifyUserExists(userId);
        ParticipationRequest requestToCancel = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundValidationException("Request with id: " + requestId + " not found"));
        if (!requestToCancel.getRequester().getId().equals(userId)) {
            throw new ForbiddenException("User with id: " + userId
                    + " can not cancel owned request with id: " + requestId);
        }
        if (requestToCancel.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event eventToRemoveOneRequest = eventRepository.findById(requestToCancel.getEvent().getId())
                    .orElseThrow(() -> new NotFoundValidationException("Event with id: "
                            + requestToCancel.getEvent().getId() + " not found"));
            eventToRemoveOneRequest.setConfirmedRequests(eventToRemoveOneRequest.getConfirmedRequests() - 1);
            eventRepository.save(eventToRemoveOneRequest);
        }
        requestToCancel.setStatus(RequestStatus.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(requestToCancel));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        verifyUserExists(userId);
        verifyEventExists(eventId);
        verifyNoPriorRequests(userId, eventId);
        verifyNotInitiator(userId, eventId);
        verifyEventPublishedAndHasSlots(eventId);
        ParticipationRequest request = buildNewRequest(userId, eventId);
        ParticipationRequest createdRequest = requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(createdRequest);
    }

    private void verifyUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundValidationException("User with id: " + userId + " not found");
        }
    }

    private void verifyEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundValidationException("Event with id: " + eventId + " not found");
        }
    }

    private void verifyNoPriorRequests(Long userId, Long eventId) {
        if (requestRepository.findRequestByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Request denied");
        }
    }

    private void verifyNotInitiator(Long userId, Long eventId) {
        if (eventRepository.findEventByInitiatorIdAndId(userId, eventId).isPresent()) {
            throw new ConflictException("Initiator cannot add request to its' event");
        }
    }

    private void verifyEventPublishedAndHasSlots(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (eventRepository.findById(eventId).isPresent()) {
            if (!event.getState().equals(EventState.PUBLISHED)) {
                throw new ConflictException("Event unpublished");
            }
            if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
                throw new ConflictException("The limit of participants is reached");
            }
        }
    }

    private ParticipationRequest buildNewRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundValidationException("User with id: " + userId + " not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundValidationException("Event with id: " + eventId + " not found"));
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.setEventConfirmedRequests(eventId, event.getConfirmedRequests() + 1);
        }
        return request;
    }
}
