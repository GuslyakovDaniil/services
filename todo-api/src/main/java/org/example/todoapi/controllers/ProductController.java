package org.example.todoapi.controllers;


import org.example.todoapi.dto.ProductDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController implements ProductApi {

    private final List<ProductDTOs.ProductResponse> products = new ArrayList<>();
    private long idCounter = 0;

    @Override
    public ProductDTOs.ProductResponse createProduct(ProductDTOs.ProductRequest request) {
        if (request == null || request.name() == null || request.name().isBlank() || request.price() == null || request.price() <= 0) {
            throw new InvalidArgumentException("Invalid product data");
        }
        var response = new ProductDTOs.ProductResponse(++idCounter, request.name(), request.price());
        products.add(response);
        return response;
    }

    @Override
    public ProductDTOs.ProductResponse getProduct(Long id) {
        return products.stream()
                .filter(product -> Objects.equals(product.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found product with id: " + id));

    }

    @Override
    public ProductDTOs.ProductResponse updateProduct(Long id, ProductDTOs.ProductRequest request) {
        if (request == null || request.name() == null || request.name().isBlank() || request.price() == null || request.price() <= 0) {
            throw new InvalidArgumentException("Invalid product data");
        }
        var product = products.stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found product with id: " + id));
        products.remove(product);
        var updateProduct = new ProductDTOs.ProductResponse(id, request.name(), request.price());
        products.add(updateProduct);
        return updateProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        var product = products.stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found product with id: " + id));
        products.remove(product);
    }
    @Override
    public List<ProductDTOs.ProductResponse> getProducts() {
        return products;
    }
}