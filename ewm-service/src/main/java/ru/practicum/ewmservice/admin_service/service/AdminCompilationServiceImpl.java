package ru.practicum.ewmservice.admin_service.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.dto.NewCompilationDto;
import ru.practicum.ewmservice.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.mapper.CompilationMapper;
import ru.practicum.ewmservice.model.Compilation;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.repository.CompilationRepository;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;
    private final AdminEventService adminEventService;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = adminEventService.findEventsFrom(newCompilationDto.getEvents());
        return compilationMapper.toCompilationDto(compilationRepository
                .save(compilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundValidationException("Compilation with id: " + compId + " not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                -> new NotFoundValidationException("Compilation with id: " + compId + "not found"));
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = adminEventService.findEventsFrom(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
