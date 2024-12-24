package com.example.todoapp.controller;

import org.example.todoapi.controllers.ProductApi;
import org.example.todoapi.dto.ProductDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import com.example.todoapp.assembler.ProductAssembler;
import com.example.todoapp.config.MessageProducer;
import com.example.todoapp.dto.NotificationDTO;
import com.example.todoapp.entity.Product;
import com.example.todoapp.repository.ProductRepository;
import com.example.todoapp.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/products")
public class ProductController implements ProductApi {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductRepository productRepository;
    private final ProductAssembler productAssembler;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public ProductController(ProductRepository productRepository,
                             ProductAssembler productAssembler,
                             MessageProducer messageProducer,
                             ObjectMapper objectMapper,
                             NotificationService notificationService) {
        this.productRepository = productRepository;
        this.productAssembler = productAssembler;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @Override
    public ProductDTOs.ProductResponse createProduct(ProductDTOs.ProductRequest request) {
        if (request == null || request.name() == null || request.name().isBlank() || request.price() == null || request.price() <= 0) {
            throw new InvalidArgumentException("Invalid product data");
        }
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        Product savedProduct = productRepository.save(product);
        NotificationDTO notificationDTO = new NotificationDTO("Created product with id:", "product", savedProduct.getId(), "create");
        sendMessage(notificationDTO, "Create product:");
        notificationService.sendNotification(notificationDTO);
        return new ProductDTOs.ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    @Override
    public ProductDTOs.ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found product with id " + id));
        NotificationDTO notificationDTO = new NotificationDTO("Get product with id:", "product", product.getId(), "get");
        sendMessage(notificationDTO, "Get product:");
        notificationService.sendNotification(notificationDTO);
        return new ProductDTOs.ProductResponse(product.getId(), product.getName(), product.getPrice());

    }

    @Override
    public ProductDTOs.ProductResponse updateProduct(Long id, ProductDTOs.ProductRequest request) {
        if (request == null || request.name() == null || request.name().isBlank() || request.price() == null || request.price() <= 0) {
            throw new InvalidArgumentException("Invalid product data");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found product with id " + id));
        product.setName(request.name());
        product.setPrice(request.price());
        Product updatedProduct = productRepository.save(product);
        NotificationDTO notificationDTO = new NotificationDTO("Updated product with id:", "product", updatedProduct.getId(), "update");
        sendMessage(notificationDTO, "Update product:");
        notificationService.sendNotification(notificationDTO);
        return new ProductDTOs.ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Could not found product with id " + id);
        }
        NotificationDTO notificationDTO = new NotificationDTO("Deleted product with id:", "product", id, "delete");
        sendMessage(notificationDTO, "Delete product:");
        notificationService.sendNotification(notificationDTO);
        productRepository.deleteById(id);
    }


    @Override
    public List<ProductDTOs.ProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        NotificationDTO notificationDTO = new NotificationDTO("Get all products", "product", null, "get");
        sendMessage(notificationDTO, "Get all products:");
        notificationService.sendNotification(notificationDTO);
        return products.stream().map(product -> new ProductDTOs.ProductResponse(product.getId(), product.getName(), product.getPrice())).collect(Collectors.toList());

    }
    private void sendMessage(NotificationDTO notificationDTO, String errorPrefix) {
        logger.debug("sendMessage called with notificationDTO: {} and errorPrefix: {}", notificationDTO, errorPrefix); // Add this line
        try {
            messageProducer.sendMessage(objectMapper.writeValueAsString(notificationDTO));
        } catch (JsonProcessingException e) {
            logger.error(errorPrefix + " Error during serialization of notification message", e);
            messageProducer.sendMessage(errorPrefix + " Error during serialization of notification message: " + e.getMessage());
        }
    }
}