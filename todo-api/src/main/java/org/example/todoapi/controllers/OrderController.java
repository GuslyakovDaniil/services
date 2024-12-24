package org.example.todoapi.controllers;

import org.example.todoapi.dto.OrderDTOs;
import org.example.todoapi.dto.ProductDTOs;
import org.example.todoapi.dto.UserDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
public class OrderController implements OrderApi {

    private final List<OrderDTOs.OrderResponse> orders = new ArrayList<>();
    private final List<UserDTOs.UserResponse> users = new ArrayList<>();
    private final List<ProductDTOs.ProductResponse> products = new ArrayList<>();
    private long idCounter = 0;


    @Override
    public OrderDTOs.OrderResponse createOrder(OrderDTOs.OrderRequest request) {
        if (request == null || request.userId() == null || request.productIds() == null || request.productIds().isEmpty() || request.orderDate() == null) {
            throw new InvalidArgumentException("Invalid order data");
        }

        var user = users.stream()
                .filter(u -> Objects.equals(u.id(), request.userId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found user with id: " + request.userId()));
        List<ProductDTOs.ProductResponse> productResponses = products.stream()
                .filter(p -> request.productIds().contains(p.id()))
                .collect(Collectors.toList());
        if (productResponses.size() != request.productIds().size()){
            throw new NotFoundException("One or more products not found");
        }
        var response = new OrderDTOs.OrderResponse(
                ++idCounter,
                user,
                productResponses,
                request.orderDate()
        );
        orders.add(response);
        return response;
    }

    @Override
    public OrderDTOs.OrderResponse getOrder(Long id) {
        return orders.stream()
                .filter(order -> Objects.equals(order.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found order with id: " + id));
    }


    @Override
    public OrderDTOs.OrderResponse updateOrder(Long id, OrderDTOs.OrderRequest request) {
        if (request == null || request.userId() == null || request.productIds() == null || request.productIds().isEmpty() || request.orderDate() == null) {
            throw new InvalidArgumentException("Invalid order data");
        }
        var order = orders.stream()
                .filter(o -> Objects.equals(o.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found order with id: " + id));
        var user = users.stream()
                .filter(u -> Objects.equals(u.id(), request.userId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found user with id: " + request.userId()));
        List<ProductDTOs.ProductResponse> productResponses = products.stream()
                .filter(p -> request.productIds().contains(p.id()))
                .collect(Collectors.toList());
        if (productResponses.size() != request.productIds().size()){
            throw new NotFoundException("One or more products not found");
        }
        orders.remove(order);
        var updateOrder = new OrderDTOs.OrderResponse(
                id,
                user,
                productResponses,
                request.orderDate()
        );
        orders.add(updateOrder);
        return updateOrder;
    }

    @Override
    public void deleteOrder(Long id) {
        var order = orders.stream()
                .filter(o -> Objects.equals(o.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found order with id: " + id));
        orders.remove(order);
    }
    @Override
    public List<OrderDTOs.OrderResponse> getOrders() {
        return orders;
    }
}