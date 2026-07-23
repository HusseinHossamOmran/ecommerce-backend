package com.tccd.ecommerce_backend.controller;
import com.tccd.ecommerce_backend.dto.CategoryRequest;
import com.tccd.ecommerce_backend.dto.CategoryResponse;
import com.tccd.ecommerce_backend.model.Category;
import com.tccd.ecommerce_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController
{
    @Autowired
    private CategoryRepository categoryRepository;
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory (@RequestBody CategoryRequest categoryRequest)
    {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        Category saved = categoryRepository.save(category);

        return ResponseEntity.ok(new CategoryResponse(saved.getId(), saved.getName(), saved.getDescription()));

    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories()
    {
        List<CategoryResponse> categories = categoryRepository.findAll().stream().map(c -> new CategoryResponse(c.getId(), c.getName(), c.getDescription())).toList();
        return ResponseEntity.ok(categories);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id)
    {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return ResponseEntity.ok(new CategoryResponse(category.getId(), category.getName(), category.getDescription()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest)
    {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if(categoryRequest.getName() != null) category.setName(categoryRequest.getName());
        if(categoryRequest.getDescription() != null) category.setDescription(categoryRequest.getDescription());
        Category updated = categoryRepository.save(category);
        return ResponseEntity.ok(new CategoryResponse(updated.getId(), updated.getName(), updated.getDescription()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id)
    {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
