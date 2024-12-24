package org.example.todoapi.controllers;

import org.example.todoapi.dto.UserDTOs;
import org.example.todoapi.exception.InvalidArgumentException;
import org.example.todoapi.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController implements UserApi {
    private final List<UserDTOs.UserResponse> users = new ArrayList<>();
    private long idCounter = 0;

    @Override
    public UserDTOs.UserResponse createUser(UserDTOs.UserRequest request) {
        if (request == null || request.firstName() == null || request.firstName().isBlank() ||
                request.lastName() == null || request.lastName().isBlank() ||
                request.email() == null || request.email().isBlank() ||
                request.phone() == null || request.phone().isBlank()) {
            throw new InvalidArgumentException("Invalid user data");
        }
        var response = new UserDTOs.UserResponse(
                ++idCounter,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone()
        );
        users.add(response);
        return response;
    }


    @Override
    public UserDTOs.UserResponse getUser(Long id) {
        return users.stream()
                .filter(user -> Objects.equals(user.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found user with id: " + id));
    }


    @Override
    public UserDTOs.UserResponse updateUser(Long id, UserDTOs.UserRequest request) {
        if (request == null || request.firstName() == null || request.firstName().isBlank() ||
                request.lastName() == null || request.lastName().isBlank() ||
                request.email() == null || request.email().isBlank() ||
                request.phone() == null || request.phone().isBlank()) {
            throw new InvalidArgumentException("Invalid user data");
        }
        var user = users.stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found user with id: " + id));
        users.remove(user);
        var updateuser = new UserDTOs.UserResponse(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone()
        );
        users.add(updateuser);
        return updateuser;
    }


    @Override
    public void deleteUser(Long id) {
        var user = users.stream()
                .filter(p -> Objects.equals(p.id(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Could not found user with id: " + id));
        users.remove(user);
    }


    @Override
    public List<UserDTOs.UserResponse> getUsers() {
        return users;
    }
}