package ru.practicum.ewmservice.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.service.AdminCompilationService;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.dto.NewCompilationDto;
import ru.practicum.ewmservice.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RequestMapping("admin/compilations")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping()
    public ResponseEntity<CompilationDto> postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST: /admin/compilations");
        return ResponseEntity.status(201).body(adminCompilationService.createCompilation(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        log.info("DELETE: /admin/compilations/{}", compId);
        adminCompilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patchCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                           @PathVariable Long compId) {
       return ResponseEntity.ok().body(adminCompilationService.updateCompilation(updateCompilationRequest, compId));
    }
}
