package ru.practicum.ewmservice.admin_service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.dto.NewCompilationDto;
import ru.practicum.ewmservice.dto.UpdateCompilationRequest;

@Service
public interface AdminCompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);
}
