package org.example.todoapi.controllers;

import org.example.todoapi.dto.ProductDTOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Tag(name = "products")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface ProductApi {
    @Operation(summary = "Создать продукт")
    @PostMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDTOs.ProductResponse createProduct(@Valid @RequestBody ProductDTOs.ProductRequest request);

    @Operation(summary = "Получить продукт по ID")
    @GetMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDTOs.ProductResponse getProduct(@PathVariable("id") Long id);

    @Operation(summary = "Обновить продукт")
    @PutMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDTOs.ProductResponse updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDTOs.ProductRequest request);

    @Operation(summary = "Удалить продукт")
    @DeleteMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteProduct(@PathVariable("id") Long id);
    @Operation(summary = "Получить все продукты")
    @GetMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
    java.util.List<ProductDTOs.ProductResponse> getProducts();

}