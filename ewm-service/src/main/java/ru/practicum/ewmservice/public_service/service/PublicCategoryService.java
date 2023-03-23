package ru.practicum.ewmservice.public_service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.CategoryDto;

import java.util.List;

@Service
public interface PublicCategoryService {

    CategoryDto getCategory(Long catId);

    List<CategoryDto> getCategories(int from, int size);
}
