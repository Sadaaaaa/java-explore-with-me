package ru.practicum.ewm.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryAdminService;

@RestController
@RequestMapping(path = "/admin/categories")
@CrossOrigin(origins = "*")
public class CategoryAdminController {

    private CategoryAdminService categoryAdminService;

    @Autowired
    public CategoryAdminController(CategoryAdminService categoryAdminService) {
        this.categoryAdminService = categoryAdminService;
    }

    @PatchMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryAdminService.updateCategory(categoryDto));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.ok(categoryAdminService.createCategory(newCategoryDto));

    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable int catId) {
        categoryAdminService.deleteCategory(catId);
    }
}
