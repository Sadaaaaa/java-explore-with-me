package ru.practicum.ewm.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dao.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryAdminServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.fromCategoryDto(categoryDto);

        // имя категории должно быть уникальным
        Category categoryDuplicate = categoryRepository.findByNameIsContainingIgnoreCase(categoryDto.getName());
        if (categoryDuplicate != null) {
            throw new BadRequestException("Category already exists!");
        }

        Category categoryToUpdate = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Category is not found!"));
        if (category.getName() != null) categoryToUpdate.setName(category.getName());

        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromNewCategoryDto(newCategoryDto);

        // имя категории должно быть уникальным
        Category categoryDuplicate = categoryRepository.findByNameIsContainingIgnoreCase(newCategoryDto.getName());
        if (categoryDuplicate != null) {
            throw new BadRequestException("Category already exists!");
        }

        categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(categoryRepository.findById(category.getId()).orElseThrow(() -> new NotFoundException("Category was not save correctly!")));
    }

    @Override
    public void deleteCategory(int catId) {
        // Обратите внимание: с категорией не должно быть связано ни одного события.
        if (eventRepository.findByCategory_Id(catId) != null) {
            throw new BadRequestException("Category linked with events!");
        }
        categoryRepository.deleteById(catId);
    }


}
