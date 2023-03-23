package ru.practicum.ewmservice.public_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.dto.CategoryDto;
import ru.practicum.ewmservice.public_service.service.PublicCategoryServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicCategoryController {

    private final PublicCategoryServiceImpl publicCategoryService;

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) {
        log.info("GET: /categories/{}", catId);
        return ResponseEntity.ok().body(publicCategoryService.getCategory(catId));
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(value = "from", defaultValue = "0")
                                               @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10")
                                           @Positive Integer size) {
        log.info("GET: /categories?from={}&size={}", from, size);
        return ResponseEntity.ok().body(publicCategoryService.getCategories(from, size));
    }
}
