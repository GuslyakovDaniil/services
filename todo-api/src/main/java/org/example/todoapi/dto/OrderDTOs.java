package org.example.todoapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;


public class OrderDTOs {
    public record OrderRequest(
            @NotNull(message = "User ID cannot be null")
            Long userId,
            @NotEmpty(message = "Product IDs list cannot be empty")
            List<Long> productIds,
            @PastOrPresent(message = "Order date should be in past or present")
            LocalDate orderDate
    ) {}

    public record OrderResponse(
            Long id,
            UserDTOs.UserResponse user,
            List<ProductDTOs.ProductResponse> products,
            LocalDate orderDate
    ) {}
}