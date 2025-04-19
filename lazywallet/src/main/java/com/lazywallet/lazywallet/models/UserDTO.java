package com.lazywallet.lazywallet.models;

//import com.lazywallet.lazywallet.models.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для передачи данных о пользователе
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private UUID userId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

//    private UserRole role;
//    private LocalDateTime createdAt;
}