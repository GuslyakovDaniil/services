package com.example.todoapp.controller;

import org.example.todoapi.controllers.CategoryApi;
import org.example.todoapi.dto.CategoryDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import com.example.todoapp.assembler.CategoryAssembler;
import com.example.todoapp.config.MessageProducer;
import com.example.todoapp.dto.NotificationDTO;
import com.example.todoapp.entity.Category;
import com.example.todoapp.repository.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryRepository categoryRepository;
    private final CategoryAssembler categoryAssembler;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    public CategoryController(CategoryRepository categoryRepository,
                              CategoryAssembler categoryAssembler,
                              MessageProducer messageProducer,
                              ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryAssembler = categoryAssembler;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
    }


    @Override
    public CategoryDTOs.CategoryResponse createCategory(CategoryDTOs.CategoryRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidArgumentException("Category name cannot be empty");
        }
        Category category = new Category();
        category.setName(request.name());
        Category savedCategory = categoryRepository.save(category);
        NotificationDTO notificationDTO = new NotificationDTO("Created category with id:", "category", savedCategory.getId(), "create");
        sendMessage(notificationDTO, "Create category:");
        return new CategoryDTOs.CategoryResponse(savedCategory.getId(), savedCategory.getName());
    }

    @Override
    public CategoryDTOs.CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found category with id: " + id));
        NotificationDTO notificationDTO = new NotificationDTO("Get category with id:", "category", category.getId(), "get");
        sendMessage(notificationDTO, "Get category:");
        return new CategoryDTOs.CategoryResponse(category.getId(), category.getName());
    }

    @Override
    public CategoryDTOs.CategoryResponse updateCategory(Long id, CategoryDTOs.CategoryRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidArgumentException("Category name cannot be empty");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found category with id " + id));
        category.setName(request.name());
        Category updatedCategory = categoryRepository.save(category);
        NotificationDTO notificationDTO = new NotificationDTO("Updated category with id:", "category", updatedCategory.getId(), "update");
        sendMessage(notificationDTO, "Update category:");
        return new CategoryDTOs.CategoryResponse(updatedCategory.getId(), updatedCategory.getName());

    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Could not found category with id: " + id);
        }
        NotificationDTO notificationDTO = new NotificationDTO("Deleted category with id:", "category", id, "delete");
        sendMessage(notificationDTO, "Delete category:");
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTOs.CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        NotificationDTO notificationDTO = new NotificationDTO("Get all categories", "category", null, "get");
        sendMessage(notificationDTO, "Get all categories:");
        return categories.stream().map(category -> new CategoryDTOs.CategoryResponse(category.getId(), category.getName())).collect(Collectors.toList());
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