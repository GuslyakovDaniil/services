package org.example.todoapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class UserDTOs {
    public record UserRequest(
            @NotBlank(message = "First name cannot be blank")
            @Size(max = 255, message = "First name must be less than 255 characters")
            String firstName,
            @NotBlank(message = "Last name cannot be blank")
            @Size(max = 255, message = "Last name must be less than 255 characters")
            String lastName,
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Email is not valid")
            String email,
            @NotBlank(message = "Phone cannot be blank")
            @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
            String phone
    ) {}

    public record UserResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone
    ) {}
}