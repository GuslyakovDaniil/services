package org.example.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


public class CategoryDTOs {
    public record CategoryRequest(
            @NotBlank(message = "Name cannot be blank")
            @Size(max = 255, message = "Name must be less than 255 characters")
            String name
    ) {}

    public record CategoryResponse(
            Long id,
            String name
    ){}
}
