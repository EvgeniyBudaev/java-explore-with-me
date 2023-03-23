package ru.practicum.ewmservice.admin_service.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.exceptions.ConflictException;
import ru.practicum.ewmservice.repository.CategoryRepository;
import ru.practicum.ewmservice.dto.CategoryDto;
import ru.practicum.ewmservice.dto.NewCategoryDto;
import ru.practicum.ewmservice.mapper.CategoryMapper;
import ru.practicum.ewmservice.model.Category;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.repository.EventRepository;
import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(newCategoryDto);
        validate(categoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category oldCategory = categoryRepository.findById(catId).orElseThrow(()
                -> new NotFoundValidationException("Category with id: " + catId + " not found"));
        validate(categoryDto);
        Category updateCategory = categoryNameUpdate(oldCategory, categoryDto);
        return categoryMapper.toCategoryDto(updateCategory);
    }

    private Category categoryNameUpdate(Category oldCategory, CategoryDto categoryDto) {
        if (categoryDto.getName() != null) {
            if (!categoryDto.getName().isBlank()) {
                oldCategory.setName(categoryDto.getName());
            }
        }
        return oldCategory;
    }

    @Override
    public void removeCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundValidationException("Category with id: " + catId + " not found");
        }
        if (eventRepository.countEventByCategoryId(catId) != 0) {
            throw new ConflictException("category id =" + catId + " still has events");
        }
        categoryRepository.deleteById(catId);
    }

    private void validate(CategoryDto categoryDto) {
        Set<String> categoryNames = new HashSet<>(categoryRepository.findCategoryByName());
        if (categoryNames.contains(categoryDto.getName())) {
            throw new ConflictException("Category name: " + categoryDto.getName() + " already used");
        }
    }
}
