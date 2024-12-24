package org.example.todoapi.dto;

import jakarta.validation.constraints.*;


public class ProductDTOs {
    public record ProductRequest(
            @NotBlank(message = "Name cannot be blank")
            @Size(max = 255, message = "Name must be less than 255 characters")
            String name,
            @NotNull(message = "Price cannot be null")
            @DecimalMin(value = "0.01", message = "Price must be greater than 0")
            Double price
    ) {}

    public record ProductResponse(
            Long id,
            String name,
            Double price
    ) {}
}
