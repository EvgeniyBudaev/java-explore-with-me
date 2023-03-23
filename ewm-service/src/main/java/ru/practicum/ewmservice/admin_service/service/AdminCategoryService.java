package ru.practicum.ewmservice.admin_service.service;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.CategoryDto;
import ru.practicum.ewmservice.dto.NewCategoryDto;

@Service
public interface AdminCategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void removeCategory(Long catId);
}
