package org.example.todoapi.controllers;

import org.example.todoapi.dto.UserDTOs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Tag(name = "users")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface UserApi {
    @Operation(summary = "Создать пользователя")
    @PostMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTOs.UserResponse createUser(@Valid @RequestBody UserDTOs.UserRequest request);

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping(value = "/api/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTOs.UserResponse getUser(@PathVariable("id") Long id);

    @Operation(summary = "Обновить пользователя")
    @PutMapping(value = "/api/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTOs.UserResponse updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDTOs.UserRequest request);

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping(value = "/api/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteUser(@PathVariable("id") Long id);
    @Operation(summary = "Получить всех пользователей")
    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    java.util.List<UserDTOs.UserResponse> getUsers();

}