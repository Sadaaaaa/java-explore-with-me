package ru.practicum.ewm.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByNameIsContainingIgnoreCase(String categoryName);
//    Page<Category> findAll(Pageable pageable);


}
