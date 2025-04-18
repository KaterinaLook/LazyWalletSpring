package com.lazywallet.lazywallet.models;

public class UserRegistrationDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    // Геттеры и сеттеры
}