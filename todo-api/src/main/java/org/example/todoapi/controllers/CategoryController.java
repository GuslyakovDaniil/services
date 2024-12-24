package org.example.todoapi.controllers;
import org.example.todoapi.dto.CategoryDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class CategoryController implements CategoryApi {

    private final List<CategoryDTOs.CategoryResponse> categories = new ArrayList<>();
    private long idCounter = 0;

    @Override
    public CategoryDTOs.CategoryResponse createCategory(CategoryDTOs.CategoryRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidArgumentException("Category name cannot be empty");
        }
        var response = new CategoryDTOs.CategoryResponse(++idCounter, request.name());
        categories.add(response);
        return response;
    }

    @Override
    public CategoryDTOs.CategoryResponse getCategory(Long id) {
        return categories.stream()
                .filter(category -> Objects.equals(category.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found category with id: " + id));
    }

    @Override
    public CategoryDTOs.CategoryResponse updateCategory(Long id, CategoryDTOs.CategoryRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidArgumentException("Category name cannot be empty");
        }
        var category =  categories.stream()
                .filter(c -> Objects.equals(c.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found category with id: " + id));
        categories.remove(category);
        var updateCategory = new CategoryDTOs.CategoryResponse(id, request.name());
        categories.add(updateCategory);
        return updateCategory;
    }


    @Override
    public void deleteCategory(Long id) {
        var category =  categories.stream()
                .filter(c -> Objects.equals(c.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found category with id: " + id));
        categories.remove(category);
    }

    @Override
    public List<CategoryDTOs.CategoryResponse> getCategories() {
        return categories;
    }
}