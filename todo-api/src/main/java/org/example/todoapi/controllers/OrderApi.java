package org.example.todoapi.controllers;

import org.example.todoapi.dto.OrderDTOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Tag(name = "orders")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface OrderApi {
    @Operation(summary = "Создать заказ")
    @PostMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderDTOs.OrderResponse createOrder(@Valid @RequestBody OrderDTOs.OrderRequest request);

    @Operation(summary = "Получить заказ по ID")
    @GetMapping(value = "/api/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderDTOs.OrderResponse getOrder(@PathVariable("id") Long id);

    @Operation(summary = "Обновить заказ")
    @PutMapping(value = "/api/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderDTOs.OrderResponse updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDTOs.OrderRequest request);

    @Operation(summary = "Удалить заказ")
    @DeleteMapping(value = "/api/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteOrder(@PathVariable("id") Long id);
    @Operation(summary = "Получить все заказы")
    @GetMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    java.util.List<OrderDTOs.OrderResponse> getOrders();
}