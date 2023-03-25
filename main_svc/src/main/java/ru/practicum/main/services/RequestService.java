package ru.practicum.main.services;


import ru.practicum.main.dto.RequestDto;
import ru.practicum.main.dto.RequestStatusUpdateDto;
import ru.practicum.main.dto.RequestStatusUpdateResult;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId);

    RequestStatusUpdateResult updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto);

    List<RequestDto> getCurrentUserRequests(Long userId);

    RequestDto cancelRequests(Long userId, Long requestId);
}
