package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

public interface CategoryAdminService {

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int catId);
}
