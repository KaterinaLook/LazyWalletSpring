package com.lazywallet.lazywallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
 * Категория для классификации транзакций (доходы/расходы)
 */
@Entity
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "type"})
@Table(name = "categories",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_category_name_user",
                columnNames = {"name", "user_id"}
        ))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 2, max = 50, message = "Category name must be between 2-50 characters")
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull(message = "Category type must be specified")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    public Category() {}

    public Category(String name, CategoryType type, User user) {
        setName(name);
        setType(type);
        setUser(user);
    }

    // ===== Сеттеры с валидацией =====
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setType(CategoryType  type) {
        this.type = Objects.requireNonNull(type, "Category type cannot be null");
    }

    public void setUser(User user) {
        this.user = Objects.requireNonNull(user, "User cannot be null");
    }
}