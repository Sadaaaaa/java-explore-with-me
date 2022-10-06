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

        // Обратите внимание: имя категории должно быть уникальным
//        if (categoryDto.getName() != null
//                && categoryRepository.findByNameIsContainingIgnoreCase(categoryDto.getName()).equals(categoryDto.getName())) {
//            throw new BadRequestException("Category already exists!");
//        }


        Category categoryToUpdate = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Category is not found!"));
        if(category.getName() != null) categoryToUpdate.setName(category.getName());

        categoryRepository.save(categoryToUpdate);

        return CategoryMapper.toCategoryDto(categoryRepository.findById(categoryToUpdate.getId())
                .orElseThrow(() -> new NotFoundException("Category was noy updated!")));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.fromNewCategoryDto(newCategoryDto);

    // Обратите внимание: имя категории должно быть уникальным
//        if (newCategoryDto.getName() != null
//                && categoryRepository.findByNameIsContainingIgnoreCase(newCategoryDto.getName()).equals(newCategoryDto.getName())) {
//            throw new BadRequestException("Category already exists!");
//        }

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
