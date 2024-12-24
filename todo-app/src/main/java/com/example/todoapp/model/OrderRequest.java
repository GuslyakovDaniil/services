package com.example.todoapp.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private List<Long> productIds;
    private LocalDate orderDate;
}
