package ru.practicum.ewmservice.private_service.service;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.ParticipationRequestDto;
import java.util.List;

@Service
public interface PrivateRequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
