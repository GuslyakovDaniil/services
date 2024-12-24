package org.example.todoapi.controllers;

import org.example.todoapi.dto.CategoryDTOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "categories")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface CategoryApi {
    @Operation(summary = "Создать категорию")
    @PostMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryDTOs.CategoryResponse createCategory(@Valid @RequestBody CategoryDTOs.CategoryRequest request);

    @Operation(summary = "Получить категорию по ID")
    @GetMapping(value = "/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryDTOs.CategoryResponse getCategory(@PathVariable("id") Long id);

    @Operation(summary = "Обновить категорию")
    @PutMapping(value = "/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryDTOs.CategoryResponse updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTOs.CategoryRequest request);

    @Operation(summary = "Удалить категорию")
    @DeleteMapping(value = "/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteCategory(@PathVariable("id") Long id);

    @Operation(summary = "Получить все категории")
    @GetMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    java.util.List<CategoryDTOs.CategoryResponse> getCategories();


}