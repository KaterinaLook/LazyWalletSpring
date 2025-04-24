package com.lazywallet.lazywallet.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сущность пользователя системы
 * The User entity represents a system user with credentials
 * and personal information.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "userName")
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"passwordHash"}) //, "accounts", "categories", "transactions"
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    // Ручные сеттеры с валидацией
    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("userName cannot be empty");
        }
        this.userName = userName.trim();
    }

    public void setEmail(String email) {
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase();
    }
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.passwordHash = passwordHash;
    }

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions = new ArrayList<>();

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private UserRole role = UserRole.USER;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Account> accounts = new HashSet<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Category> categories = new HashSet<>();
//

    // Геттеры для коллекций как read-only
//    public Set<Category> getCategories() {
//        return Collections.unmodifiableSet(categories);
//    }
}
