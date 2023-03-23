package ru.practicum.ewmservice.admin_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.service.AdminCategoryService;
import ru.practicum.ewmservice.dto.CategoryDto;
import ru.practicum.ewmservice.dto.NewCategoryDto;

import javax.validation.Valid;

@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST: /admin/categories");
        return ResponseEntity.status(201).body(adminCategoryService.addCategory(newCategoryDto));
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> patchCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                        @PathVariable Long catId) {
        log.info("PATCH: /admin/categories");
        return ResponseEntity.ok().body(adminCategoryService.updateCategory(categoryDto, catId));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("DELETE: /admin/categories/{}", catId);
        adminCategoryService.removeCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
