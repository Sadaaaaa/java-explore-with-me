package ru.practicum.ewm.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.service.CategoryPublicService;

@RestController
@RequestMapping(path = "/categories")
@CrossOrigin(origins = "*")
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    @Autowired
    public CategoryPublicController(CategoryPublicService categoryPublicService) {
        this.categoryPublicService = categoryPublicService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(categoryPublicService.getAllCategories(from, size));
    }


    @GetMapping("{catId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer catId) {
        return ResponseEntity.ok(categoryPublicService.getCategoryById(catId));
    }
}
