package com.librarymanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * =====================================================
 * UserDTO - Data Transfer Object for Registration
 * =====================================================
 * DTO = Data Transfer Object
 * It carries data between the HTML form and the controller.
 *
 * Why use DTO instead of Entity directly?
 * → Security: We don't expose our database entity directly
 * → Validation: We can add form-specific rules here
 * → Flexibility: DTO can have fields not in the entity
 * =====================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Role selected during registration: "ROLE_USER" or "ROLE_ADMIN"
    private String role;
}
