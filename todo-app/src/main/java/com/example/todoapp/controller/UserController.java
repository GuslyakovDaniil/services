package com.example.todoapp.controller;

import org.example.todoapi.controllers.UserApi;
import org.example.todoapi.dto.UserDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import com.example.todoapp.assembler.UserAssembler;
import com.example.todoapp.config.MessageProducer;
import com.example.todoapp.dto.NotificationDTO;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController implements UserApi {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public UserController(UserRepository userRepository,
                          UserAssembler userAssembler,
                          MessageProducer messageProducer,
                          ObjectMapper objectMapper, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @Override
    public UserDTOs.UserResponse createUser(UserDTOs.UserRequest request) {
        if (request == null || request.firstName() == null || request.firstName().isBlank() ||
                request.lastName() == null || request.lastName().isBlank() ||
                request.email() == null || request.email().isBlank() ||
                request.phone() == null || request.phone().isBlank()) {
            throw new InvalidArgumentException("Invalid user data");
        }
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        User savedUser = userRepository.save(user);
        NotificationDTO notificationDTO = new NotificationDTO("Created user with id:", "user", savedUser.getId(), "create");
        sendMessage(notificationDTO, "Create user:");
        notificationService.sendNotification(notificationDTO);
        return new UserDTOs.UserResponse(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(), savedUser.getPhone());
    }

    @Override
    public UserDTOs.UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found user with id " + id));
        NotificationDTO notificationDTO = new NotificationDTO("Get user with id:", "user", user.getId(), "get");
        sendMessage(notificationDTO, "Get user:");
        notificationService.sendNotification(notificationDTO);
        return new UserDTOs.UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
    }

    @Override
    public UserDTOs.UserResponse updateUser(Long id, UserDTOs.UserRequest request) {
        if (request == null || request.firstName() == null || request.firstName().isBlank() ||
                request.lastName() == null || request.lastName().isBlank() ||
                request.email() == null || request.email().isBlank() ||
                request.phone() == null || request.phone().isBlank()) {
            throw new InvalidArgumentException("Invalid user data");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found user with id " + id));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        User updatedUser = userRepository.save(user);
        NotificationDTO notificationDTO = new NotificationDTO("Updated user with id:", "user", updatedUser.getId(), "update");
        sendMessage(notificationDTO, "Update user:");
        notificationService.sendNotification(notificationDTO);
        return new UserDTOs.UserResponse(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getEmail(), updatedUser.getPhone());

    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Could not found user with id " + id);
        }
        NotificationDTO notificationDTO = new NotificationDTO("Deleted user with id:", "user", id, "delete");
        sendMessage(notificationDTO, "Delete user:");
        notificationService.sendNotification(notificationDTO);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTOs.UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        NotificationDTO notificationDTO = new NotificationDTO("Get all users", "user", null, "get");
        sendMessage(notificationDTO, "Get all users:");
        notificationService.sendNotification(notificationDTO);
        return users.stream().map(user -> new UserDTOs.UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone())).collect(Collectors.toList());

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