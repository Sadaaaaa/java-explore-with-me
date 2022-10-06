package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryPublicService {
    List<Category> getAllCategories(Integer from, Integer size);

    Category getCategoryById(Integer catId);
}
